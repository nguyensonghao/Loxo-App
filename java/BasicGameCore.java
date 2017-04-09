package com.koolsoft.littlezeros.client.activities.game.gamecore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent.ORIENTATION;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.dialog.ConfirmDialog.ConfirmCallback;
import com.koolsoft.littlezeros.client.ClientData;
import com.koolsoft.littlezeros.client.ClientUtils;
import com.koolsoft.littlezeros.client.LittleZeros;
import com.koolsoft.littlezeros.client.activities.event.CardNoteEvent;
import com.koolsoft.littlezeros.client.activities.event.CardNoteEvent.CardNoteStatus;
import com.koolsoft.littlezeros.client.activities.game.GameSetting;
import com.koolsoft.littlezeros.client.activities.game.quiz.QuizGameObject;
import com.koolsoft.littlezeros.client.activities.game.spelling.SpellingGameObject;
import com.koolsoft.littlezeros.client.activities.game.truefalse.TrueFalseGameObject;
import com.koolsoft.littlezeros.client.audio.AudioManager;
import com.koolsoft.littlezeros.client.data.LoginManager;
import com.koolsoft.littlezeros.client.localization.Locale;
import com.koolsoft.littlezeros.client.view.AlertDialogUiView;
import com.koolsoft.littlezeros.client.view.Toaster;
import com.koolsoft.littlezeros.client.views.discussions.CommentSliding;
import com.koolsoft.littlezeros.shared.AppConfig;
import com.koolsoft.littlezeros.shared.Config;

public class BasicGameCore {
	
	protected IGameCoreView gameCoreView;
	protected GameObject gameObject = null;
	protected GameSetting gameSetting = new GameSetting();
	protected int cardWidth;
	protected int cardHeight;
	protected int colNum;
	protected int rowNum;
	protected int defaultButtonBarHeight = 60;
	protected boolean withHint = false;
	protected boolean showSound = false;
	protected boolean frontFirst = true;
	protected boolean playSound = false;
	protected int intervalGameTime = 5;
	protected int difficultyLevel = 0;
	protected AsyncCallback<Map<String, String>> answerCallback = null;
	protected boolean isPlaying = false;
	protected int gameMode = Config.MODE_PRATICE;
	protected boolean isAnswered = false;
	protected boolean dictionaryMode = false;
	
	public BasicGameCore(IGameCoreView gameCoreView) {
		this.gameCoreView = gameCoreView;
	}
	
	public void set(boolean showSound, boolean withHint, boolean frontFirst, int rowNum, int colNum) {
		this.showSound = showSound;
		this.withHint = withHint;
		this.frontFirst = frontFirst;
		this.colNum = colNum;
		this.rowNum = rowNum;
	}
	
	public void setSizeByOrientation() {
		int max = this.colNum;
		int min = this.rowNum;
		if (this.colNum > this.rowNum) {
			max = this.colNum;
			min = this.rowNum;
		}
		else {
			max = this.rowNum;
			min = this.colNum;			
		}
		if (MGWT.getOrientation() == ORIENTATION.PORTRAIT) {
			this.colNum = min;
			this.rowNum = max;
		}
		else {
			this.colNum = max;
			this.rowNum = min;
		}
		setSize();
	}
	
	public void setSize() {
		cardWidth = (ClientUtils.getScreenWidth() - 2 - 2 * Config.PADDING_WIDTH)/this.colNum;
		cardHeight = (ClientUtils.getScreenHeight() - 2 - 2 * Config.PADDING_WIDTH)/this.rowNum;
	}
	
	public void play(GameObject object, final AsyncCallback<Map<String, String>> answerCallback) {
		this.gameObject = object;
		this.answerCallback = answerCallback;
		setPlaying(true);
		gameCoreView.onStart(object, gameSetting);
		gameCoreView.showButtonBar();
		gameCoreView.showHeader();
		gameCoreView.updateProgressWidgetWidth();
		this.addCardHandlers();
	}
	
	public void onReview(){
		gameCoreView.showHeader();
		gameCoreView.getResultPanel().hide();
	}
	
	public void playAgain(){
		gameCoreView.showHeader();
	}
	
	public GameObject getGameObject() {
		return gameObject;
	}

	public int getCardWidth() {
		return cardWidth;
	}

	public void setCardWidth(int cardWidth) {
		this.cardWidth = cardWidth;
	}

	public int getCardHeight() {
		return cardHeight;
	}

	public void setCardHeight(int cardHeight) {
		this.cardHeight = cardHeight;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public boolean isWithHint() {
		return withHint;
	}

	public void setWithHint(boolean withHint) {
		this.withHint = withHint;
	}

	public boolean isShowSound() {
		return showSound;
	}

	public void setShowSound(boolean showSound) {
		this.showSound = showSound;
	}

	public boolean isFrontFirst() {
		return frontFirst;
	}

	public void setFrontFirst(boolean frontFirst) {
		this.frontFirst = frontFirst;
	}

	public void onOrientationChanged() {
	}

	public void onPlayerAnswer(Boolean result, Long questionId, String playerName) {
	}
	
	public void showCorrectAnswer() {
	}

	protected void showResultPanel(int isCorrect) {
		showResultPanel(isCorrect, "", 0);
	}
	
	public void showResultPanel(int isCorrect, int score) {
		showResultPanel(isCorrect, "", score);
	}
	
	public void updateScore(String score) {
		gameCoreView.updateScore(score);
	}
	
	protected void showResultPanel(int isCorrect, String playerName, int score) {
		this.answerCallback = null;
		gameObject.setCorrectScore(score);
		setAnswed(true);
		String content = "";
		String color = "";
		gameCoreView.onAnswer(isCorrect, playerName, score, gameObject.getGameObjectType());
		color = isCorrect == 1 ? Config.CORRECT_COLOR : Config.INCORRECT_COLOR;
		String answerKey = gameObject.getAnswerKey();
		if (answerKey != null && !answerKey.isEmpty()) {
			content = "<font style='font-weight:bold;'>" + Locale.get().answer()  + ": </font>"
					+ "<font> "+ answerKey + "</font>";
		}
		String hint = gameObject.getAnswerHint();
		if (hint != null && !hint.isEmpty() && (
			answerKey == null || answerKey.isEmpty() 
			|| !new HTML(answerKey).getText().equalsIgnoreCase(new HTML(hint).getText()))) {
			if(gameObject.getDatabase() == Config.VOCABULARY_DATABASE){
				if (answerKey != null && !answerKey.isEmpty()) {
					hint = hint.replace(answerKey, "<font style='color:blue;'>" + answerKey + "</font>");
				}
				String questionText = "";
				if (gameObject instanceof QuizGameObject)
					questionText = ((QuizGameObject)gameObject).getQuestion().getText();
				else if (gameObject instanceof SpellingGameObject)
					questionText = ((SpellingGameObject)gameObject).getQuestion().getText();
				else if (gameObject instanceof TrueFalseGameObject)
					questionText = ((TrueFalseGameObject)gameObject).getQuestion().getText();
				if(questionText != null && !questionText.isEmpty() && !questionText.equalsIgnoreCase(answerKey) && !questionText.contains(answerKey)){
					hint = hint.replace(questionText, "<font style='color:blue'>" + questionText + "</font>");
				}
			}
			if(gameObject.getPartOfSpeech() != null && !gameObject.getPartOfSpeech().isEmpty()){
				content += "<br> " + "<font style='font-weight:bold;'>" + Locale.get().hint() + ": </font>"+ " ("+gameObject.getPartOfSpeech() +") "+hint;
			}else{
				content += "<br> " + "<font style='font-weight:bold;'>" + Locale.get().hint() + ": </font>"+ hint;
			}
		}
		showResultContent(content, color);
		showCardStatistic(isCorrect, color);
		showNote(color);
	}
	
	protected void showResultContent(String content, String color){
		HTML contentResult = new HTML(content);
		contentResult.getElement().getStyle().setColor(color);
		gameCoreView.getResultPanel().showContentResult(contentResult, true, false);
	}
	
	protected void showCardStatistic(int isCorrect, String color){
		if(gameObject.getCardStatistic() != null) {
			int percenCorrect = 0;
			//int percenInCorrect = 0;
			gameCoreView.getResultPanel().getProgressPanel().setVisible(false);
//			if(gameObject.getCardStatistic().getCorrects() + gameObject.getCardStatistic().getIncorrects() < 10){
//				return;
//			}
			if(gameObject.getCardStatistic().getCorrects() != 0 
					|| gameObject.getCardStatistic().getIncorrects() != 0) {
				percenCorrect = gameObject.getCardStatistic().getCorrects() 
						* 100 / (gameObject.getCardStatistic().getCorrects() 
								+ gameObject.getCardStatistic().getIncorrects());
				//percenInCorrect = 100 - percenCorrect;
			} else {
				if(isCorrect == Config.ANSWER_CORRECT){
					percenCorrect = 100;
				} 
//				else {
//					percenInCorrect = 100;
//				}
			}
			//if(isCorrect == Config.ANSWER_CORRECT){
				percenCorrect = percenCorrect > 0 ? percenCorrect : 1;
				gameCoreView.getResultPanel().updateProgress(percenCorrect, Locale.get().userAnswerCorrectly(percenCorrect), color);
			//} 
//			else {
//				percenInCorrect = percenInCorrect > 0 ? percenInCorrect : 1;
//				gameCoreView.getResultPanel().updateProgress(percenInCorrect, Locale.get().userAnswerIncorrectly(percenInCorrect), color);
//			}
			ClientUtils.log("xxx : " + gameObject.getCardStatistic().getCorrects() + " - " + gameObject.getCardStatistic().getIncorrects());
		} 
	}
	
	protected void showNote(final String color){
		ClientData.NOTE_MANAGER.onloadNoteForQuestion(gameObject.getId(), new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
			}
		
			@Override
			public void onSuccess(String result) {
				if(result != null && !result.isEmpty()){
					String note = Locale.get().note() + ": " + result;
					HTML html = new HTML(note);
					html.getElement().getStyle().setColor(color);
					gameCoreView.getResultPanel().showContentResult(html, false, false);
				}
			}
		});
	}
		

	protected double getAnwserFontSize(){
		return LoginManager.getUserSetting().getGameFont() - 0.3;
	}
	
	/**
	 * @return the intervalGameTime
	 */
	public int getIntervalGameTime() {
		return intervalGameTime;
	}

	/**
	 * @param intervalGameTime the intervalGameTime to set
	 */
	public void setIntervalGameTime(int intervalGameTime) {
		this.intervalGameTime = intervalGameTime;
	}	
	
	public int getDifficultyLevel() {
		return difficultyLevel;
	}
	
	public void saveScreenGame() {
		if(LittleZeros.phoneGap.isPhoneGapDevice() )
			ClientUtils.takeScreenShot();
	}
	
	public void showTutorial(Widget widget, String tutorialString) {
	}

	public void showAnswer(String userId, String challengePlayer, Long id,
			int challengerAnswerIndex, int challengerAnswerTime,
			boolean challengerAnswerResult) {
		
	}
	
	public void resizeViewToPreview(){
	}
	
	/**
	 * @return the isPlaying
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	/**
	 * @param isPlaying the isPlaying to set
	 */
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}


	public void newGame() {
	}

	public void adjustFontSizeAndRefreshScroll() {
		// TODO Auto-generated method stub
		
	}
	
	public void setShuffeAnswer(boolean shuffeAnswer) {
	}

	public void cancelTimer() {
		
	}

	public int getGameMode() {
		return gameMode;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}
	
	public void checkAndRenderMathJax() {
		
	}

	public void removeHanders() {
	}
	
	public void stopAudio(){
		AudioManager.forceStopSound();
		gameCoreView.stopAudio();
	}
	
	public void onStop() {
		gameCoreView.onStop(gameObject);
	}
	
	public boolean isPlaySound() {
		return playSound;
	}

	public void showHideKeyboard(boolean showHide) {
		gameCoreView.showHideKeyboard(showHide);
	}

	public void changeTheme(String themeProperty, String value) {
	}
	
	protected List<HandlerRegistration> cardHandlers = new ArrayList<HandlerRegistration>();
	protected void addCardHandlers() {
		final long cardId = gameObject.getId();
		final long topicId = gameObject.getTopicId();
		final long categoryId = gameObject.getCategoryId();
		final int database = gameObject.getDatabase();
		final int box = gameObject.getBox();
		gameCoreView.getBookmarkButton().setActive(ClientData.myCardIDDataManager.isCardBookmark(cardId, categoryId));
		gameCoreView.getResultPanel().setBookmarkActive(ClientData.myCardIDDataManager.isCardBookmark(cardId, categoryId));
		gameCoreView.getResultPanel().setVisibleIgnoreButton(!ClientData.myCardIDDataManager.isCardIgnore(cardId, categoryId));
		for (HandlerRegistration handler : cardHandlers) {
			handler.removeHandler();
		}
		
		cardHandlers.add(gameCoreView.getBookmarkButton().addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				LittleZeros.getClientFactory().getEventBus().fireEvent(new CardNoteEvent(cardId, topicId, 
						categoryId, database, CardNoteStatus.BOOKMARK, box));
				gameCoreView.getBookmarkButton().setActive(!gameCoreView.getBookmarkButton().isActive());
				Toaster.showToast(gameCoreView.getBookmarkButton().isActive() ? Locale.get().youHaveBookmarked()
															: Locale.get().youHaveUnBookmarked());
				gameCoreView.getResultPanel().setBookmarkActive(gameCoreView.getBookmarkButton().isActive());
			}
		}));
		cardHandlers.add(gameCoreView.getResultPanel().getBtnBookmark().addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				gameCoreView.getResultPanel().showMenu(false);
				LittleZeros.getClientFactory().getEventBus().fireEvent(new CardNoteEvent(cardId, topicId, 
						categoryId, database, CardNoteStatus.BOOKMARK, box));
				gameCoreView.getBookmarkButton().setActive(!gameCoreView.getBookmarkButton().isActive());
				Toaster.showToast(gameCoreView.getBookmarkButton().isActive() ? Locale.get().youHaveBookmarked()
															: Locale.get().youHaveUnBookmarked());
				gameCoreView.getResultPanel().setBookmarkActive(gameCoreView.getBookmarkButton().isActive());
			}
		}));
		cardHandlers.add(gameCoreView.getResultPanel().getBtnIgnore().addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				gameCoreView.getResultPanel().showMenu(false);
				new AlertDialogUiView(Locale.get().ignore(), new HTML(Locale.get().doYouWantToIgnore()), 
						Locale.get().ok(), Locale.get().cancel(), new ConfirmCallback() {
					@Override
					public void onOk() {
						LittleZeros.getClientFactory().getEventBus().fireEvent(new CardNoteEvent(cardId, topicId,
								categoryId, database, CardNoteStatus.IGNORE, box));
						gameCoreView.getResultPanel().setVisibleIgnoreButton(false);
					}
					@Override
					public void onCancel() {
					}
				}).show();
			}
		}));
		cardHandlers.add(gameCoreView.getResultPanel().getBtnDisscustion().addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				CommentSliding.getInstance().showComments(cardId, Config.DISCUSSTION_TYPE_QUESTION_COMMENT);
			}
		}));
		cardHandlers.add(gameCoreView.getResultPanel().getBtnReport().addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				long id = gameObject.getId();
				if (id <= 0) {
					Toaster.showToast(Locale.get().errorOccurs());
					return;
				}
				gameCoreView.getResultPanel().showMenu(false);
				gameCoreView.getResultPanel().getFeedbackDialog().show(id, AppConfig.MAIN_TOPIC_ID);	
			}
		}));
		cardHandlers.add(gameCoreView.getResultPanel().getBtnNote().addTapHandler(new TapHandler() {
			
			@Override
			public void onTap(TapEvent event) {
				gameCoreView.getResultPanel().showMenu(false);
				gameCoreView.getResultPanel().getDialogNote().showData(gameObject.getId(),gameObject.getDatabase());
			}
		}));
		
	}
	
	public void showDictionaryMode(){
		gameCoreView.showDictionaryMode(this.getDictionaryMode());
	}
	
	public IGameCoreView getGameCoreView() {
		return gameCoreView;
	}
	
	public Widget getGamePanel(){
		return gameCoreView.asWidget();
	}
	
	
	public boolean isAnswed() {
		return isAnswered;
	}
	
	public void setAnswed(boolean isAnswed) {
		this.isAnswered = isAnswed;
	}
	
	public void setDictionaryMode(boolean dictionaryMode) {
		this.dictionaryMode = dictionaryMode;
	}
	
	public boolean getDictionaryMode() {
		return this.dictionaryMode;
	}
}