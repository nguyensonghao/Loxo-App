package com.koolsoft.littlezeros.client.activities.game.quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.koolsoft.littlezeros.client.ClientUtils;
import com.koolsoft.littlezeros.client.activities.game.gamecore.BasicGameCore;
import com.koolsoft.littlezeros.client.activities.game.gamecore.GameObject;
import com.koolsoft.littlezeros.client.audio.SFXAudioPlayer;
import com.koolsoft.littlezeros.shared.Config;

public class QuizGameCore extends BasicGameCore {
	
	private List<HandlerRegistration> handlers = new ArrayList<HandlerRegistration>();
	protected List<Widget> selectedAnswers = new ArrayList<Widget>();
	private long initTime = 0;
	private boolean showResult = true;
	private Map<String, String> resultMap = new HashMap<String, String>();
	private IQuizGameView quizGameCoreView;
	
	public QuizGameCore(IQuizGameView quizGameCoreView, boolean showResult) {
		super(quizGameCoreView);
		this.quizGameCoreView = quizGameCoreView;
		this.showResult = showResult;
		intervalGameTime = 5;
	}
	
	@Override
	public void play(GameObject object, final AsyncCallback<Map<String, String>> answerCallback) {
		QuizGameObject currentGameObject = (QuizGameObject) object;
		selectedAnswers.clear();
		playSound = currentGameObject.getQuestion().isGameWithSound();
		gameSetting.setFrontShowing(withHint ? true : !frontFirst);
		super.play(object, answerCallback);
		initTime = System.currentTimeMillis();
	}
	
	@Override
	public void newGame() {
		initTime = System.currentTimeMillis();
	}
	
	@Override
	protected void addCardHandlers() {
		super.addCardHandlers();
		handlers.clear();
		resultMap.clear();
		for (final int key : quizGameCoreView.getAnswerCards().keySet()) {
			final QuizAnswerCardTest aCard = (QuizAnswerCardTest)quizGameCoreView.getAnswerCards().get(key);
			handlers.add(aCard.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent event) {
					if (getDictionaryMode() || isAnswered) {
						return;
					}
					onAnswer(key, aCard);
				}
			}));
		}
	}
	
	public void gameFinish(){
		isAnswered = true;
		for (final int key : quizGameCoreView.getAnswerCards().keySet()) {
			final QuizAnswerCardTest aCard = (QuizAnswerCardTest)quizGameCoreView.getAnswerCards().get(key);
			if(aCard.isCorrectAnswer()){
				aCard.updateStyle(true);
				resultMap.put(Config.RESULT, false+"");
			}
		}
		for (HandlerRegistration hdler : handlers) {
			hdler.removeHandler();
		}
		answerCallback.onSuccess(resultMap);
		quizGameCoreView.refreshView();
	}
	
	protected void onAnswer(int key, QuizAnswerCardTest aCard) {
		int numberAnswer = ((QuizGameObject)gameObject).getNumberAnswer();
		if (!selectedAnswers.contains(aCard)) {
			numberAnswer += 1;
		}
		((QuizGameObject)gameObject).setNumberAnswer(numberAnswer);
		int numberCorrectAnswer = ((QuizGameObject)gameObject).getNumberCorrectAnswer();
		selectedAnswers.add(aCard);
		resultMap.put(Config.QUESTION_ID, gameObject.getId()+"");
		resultMap.put(Config.ANSWER_INDEX, key +"");
		long answerTime = System.currentTimeMillis() - initTime;
		resultMap.put(Config.ANSWER_TIME, answerTime + "");
		if (aCard.isCorrectAnswer()) {
				aCard.updateStyle(true);
			if(!resultMap.containsKey(Config.RESULT) || resultMap.get(Config.RESULT).equalsIgnoreCase("true"))
				resultMap.put(Config.RESULT, true+"");
		}
		else {
			aCard.updateStyle(false);
			resultMap.put(Config.RESULT, false+"");
		}
		onStopSound();
		if (((QuizGameObject)gameObject).getQuestion().isGameWithSound()) {
			quizGameCoreView.getQuestionView().soundToText();
		}
		else if(gameMode == Config.MODE_MULTIGAME) {
			SFXAudioPlayer.playSoundEffects(aCard.isCorrectAnswer() ? SFXAudioPlayer.SFX_CORRECT : SFXAudioPlayer.SFX_INCORRECT);
		}
		if(numberAnswer >= numberCorrectAnswer) {
			ClientUtils.log("show result: " + showResult);
			//TODO: if(!aCard.isCorrectAnswer() && showResult) {
			if(showResult) {
				showCorrectAnswer();
			}
			isAnswered = true;
			answerCallback.onSuccess(resultMap);
			for (HandlerRegistration hdler : handlers) {
				hdler.removeHandler();
			}
		}
		quizGameCoreView.refreshView();
	}
	
	protected void onStopSound(){
		this.stopAudio();
	}
	
	Timer testAnswerTimer = new Timer() {
		
		@Override
		public void run() {
			for (final int key : quizGameCoreView.getAnswerCards().keySet()) {
				final QuizAnswerCardTest aCard = quizGameCoreView.getAnswerCards().get(key);
				if(aCard.isCorrectAnswer()) {
					onAnswer(key, aCard);
					break;
				}
			}
		}
	};
	
	@Override
	public void removeHanders() {
		for (HandlerRegistration hdler : handlers) {
			hdler.removeHandler();
		}
	}

	@Override
	public void showCorrectAnswer() {
		super.showCorrectAnswer();
		quizGameCoreView.showCorrectAnswer();
	}

	@Override
	public void showAnswer(String userId, String playerAnswer, Long questionId,
			int answerIndex, int answerTime,
			boolean result) {
		((QuizAnswerCardTest)quizGameCoreView.getAnswerCards().get(answerIndex)).showUserAnswer(userId, playerAnswer, result);
		if(result)
			removeHanders();
	}
	
	public void setQuizize(int size){
		quizGameCoreView.setQuizSize(size);
	}

	@Override
	public void cancelTimer() {
		testAnswerTimer.cancel();
	}
	
	@Override
	public void changeTheme(String themeProperty, String value) {
		if(quizGameCoreView.getQuestionView() != null)
			quizGameCoreView.getQuestionView().getElement().getStyle().setProperty(themeProperty, value);
		for(Widget widget : quizGameCoreView.getAnswerCards().values())
			widget.getElement().getStyle().setProperty(themeProperty, value);
	}
}