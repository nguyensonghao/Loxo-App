package com.koolsoft.littlezeros.client.activities.game.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.koolsoft.littlezeros.client.LittleZeros;
import com.koolsoft.littlezeros.client.activities.basic.BasicViewImpl.Layout;
import com.koolsoft.littlezeros.client.activities.game.GameSetting;
import com.koolsoft.littlezeros.client.activities.game.gamecore.GameCoreView;
import com.koolsoft.littlezeros.client.activities.game.gamecore.GameObject;
import com.koolsoft.littlezeros.client.data.LoginManager;
import com.koolsoft.littlezeros.client.localization.Locale;
import com.koolsoft.littlezeros.shared.Config;

public class QuizGameView extends GameCoreView implements IQuizGameView{
	
	private QuizAnswerCardTest answerCard;
	private Map<Integer, QuizAnswerCardTest> answerCards = new HashMap<Integer, QuizAnswerCardTest>();
	private int quizSize = 4;
	private HTML numberAnswers = new HTML();
	
	public QuizGameView(Widget gameProgressWidget, Layout layout) {
		super(gameProgressWidget, layout);
		numberAnswers.getElement().getStyle().setPaddingLeft(10, Unit.PX);
		numberAnswers.getElement().getStyle().setFontSize(1.3, Unit.EM);
	}
	
	@Override
	public void onStart(GameObject gameObject, GameSetting gameSetting) {
		super.onStart(gameObject, gameSetting);
		answerCards.clear();
		QuizGameObject currentGameObject = (QuizGameObject) gameObject;
		gameSetting.setSize(cardScreenWidth, -1);
		gameSetting.setImageSize(cardScreenWidth, cardScreenHeight /2);
		if(questionView == null) {
			questionView = new QuizCard(currentGameObject.getQuestion(),gameSetting);
		}
		else {
			questionView.showFace(currentGameObject.getQuestion(), gameSetting);
		}
		questionView.setFontSize(LoginManager.getUserSetting().getGameFont());
		questionView.getElement().getStyle().setProperty("minHeight", cardScreenHeight * 1/5 + "px");
		quizSize = currentGameObject.getAnswers().size();
		int row = 0, col = 0;
		gameViewPanel.setWidget(row, col, questionView.asWidget());
		gameViewPanel.getCellFormatter().setAlignment(row, col, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_TOP);
		gameViewPanel.getFlexCellFormatter().setColSpan(row, col, gameSetting.getColNum());
		int numberCorrect = 0;
		for (int index= 0 ; index < currentGameObject.getAnswers().size() ; index++) {
			QuizAnswerCardTest aCard = new QuizAnswerCardTest(currentGameObject.getAnswers().get(index), gameSetting);
			aCard.setFontSize(LoginManager.getUserSetting().getGameFont() - 0.3);
			if(gameSetting.isShuffeAnswer()) {
				int pos = Random.nextInt(quizSize);
				while (answerCards.containsKey(pos)) {
					pos = Random.nextInt(quizSize);
				}
				answerCards.put(pos,aCard);
			} else {
				answerCards.put(index, aCard);
			}
			if (currentGameObject.getAnswers().get(index).getCardId() == currentGameObject.getQuestion().getCardId()) {
				aCard.setCorrectAnswer(true);
				answerCard = aCard;
				numberCorrect ++;
			}
		}
		if(numberCorrect > 1){
			numberAnswers.setText(Locale.get().selectNKeys(numberCorrect));
		}
		LittleZeros.getLoadingDialog().hide();
		this.displayQuiz();
		refreshView();
		new Timer() {
			
			@Override
			public void run() {
				refreshView();
			}
		}.schedule(2000);
	}
	
	@Override
	public void onAnswer(int isCorrect, String playerName, int score, int gameType) {
		super.onAnswer(isCorrect, playerName, score, gameType);
		if (answerCard.getFace().hasSound() && !answerCard.getFace().isGameWithSound() 
				&& (answerCard.getFace().getGameType() == Config.GAME_QUIZ_REVERSE 
				|| answerCard.getFace().getGameType() == Config.GAME_QUIZ_WITH_HINT)) {
			answerCard.playSound();
		}
	}
	
	private void displayQuiz() {
		List<Widget> answerCardsList = new ArrayList<Widget>(answerCards.values());
		displayAnswer(answerCardsList);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				new Timer() {
					@Override
					public void run() {
						double defaultFontSize = LoginManager.getUserSetting().getGameFont();
						while (mainViewScroll.getMaxScrollY() < 0 && defaultFontSize > LoginManager.getUserSetting().getGameFont() - 0.3) {
							defaultFontSize = defaultFontSize - 0.1;
							questionView.setFontSize(defaultFontSize);
							for (QuizAnswerCardTest widget : answerCards.values()) {
								widget.setFontSize(defaultFontSize - 0.3);
							}
							mainViewScroll.refresh();
						}
						if(mainViewScroll.getMaxScrollY() < 0){
							mainViewScroll.getElement().getStyle().setProperty("borderBottom", "1px solid black");
						}
					}
				}.schedule(50);
			}
		});
	}
	
	private void displayAnswer(final List<Widget> answerCardsList) {
		int row = 2;
		List<Integer> indexList = new ArrayList<Integer>();
		if(gameSetting.isShuffeAnswer()) {
			for (int i = 0; i < answerCards.size(); i++) {
				indexList.add(i);
			}
		}
		else {
			indexList = new ArrayList<Integer>(answerCards.keySet());
			Collections.sort(indexList, new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});
		}
		int cardNumDisplayed = 0;
		if(!numberAnswers.getText().isEmpty()){
			gameViewPanel.setWidget(row++, 0, numberAnswers);
		}
		if(gameSetting.isShuffeAnswer()) {
			while (cardNumDisplayed++ < answerCards.size()) {
				int index = Random.nextInt(indexList.size());
				QuizAnswerCardTest card = answerCards.get(indexList.get(index));
				gameViewPanel.setWidget(row++, 0, card.asWidget());
				indexList.remove(index);
				
			}
		} else {
			for(int i = 0;i<indexList.size();i++) {
				int index = indexList.get(i);
				QuizAnswerCardTest card = answerCards.get(indexList.get(index));
				gameViewPanel.setWidget(row++, 0, card.asWidget());
			}
		}
		LittleZeros.hideLoading();
		mainViewScroll.refresh();
	}
	
	protected void displayAnswer(final QuizAnswerCardTest card) {
		int row = gameViewPanel.getRowCount();
		gameViewPanel.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
		gameViewPanel.setWidget(row, 0, card.asWidget());
		gameViewPanel.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
	}
	
	@Override
	public void showCorrectAnswer() {
		for (final int key : answerCards.keySet()) {
			final QuizAnswerCardTest aCard = answerCards.get(key);
			if(aCard.isCorrectAnswer()) {
				aCard.updateStyle(true);
			}
		}
	}
	
	public void setSize(){
		buttonBar_height = 0;
		super.setSize();
	}
	
	@Override
	public void refreshView() {
		super.refreshView();
		boolean showScrollBar = true;
		if(mainViewScroll.getY() < -10){
			showScrollBar = false;
		}
		mainViewScroll.setShowVerticalScrollBar(showScrollBar);
		mainViewScroll.setHideScrollBar(!showScrollBar);
		mainViewScroll.refresh();
	}
	
	@Override
	public void showDictionaryMode(boolean isDictionaryMode) {
		questionView.refreshView(isDictionaryMode, false);
		for (QuizAnswerCardTest card : answerCards.values()) {
			card.refreshView(isDictionaryMode);
		}
	}
	
	@Override
	public Map<Integer, QuizAnswerCardTest> getAnswerCards() {
		return answerCards;
	}

	@Override
	public void setQuizSize(int size) {
		this.quizSize = size;
	}
}
