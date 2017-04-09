package com.koolsoft.littlezeros.client.activities.game.quiz;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.koolsoft.littlezeros.client.ClientUtils;
import com.koolsoft.littlezeros.client.CssToken;
import com.koolsoft.littlezeros.client.activities.event.DictionaryTapEvent.DictionaryType;
import com.koolsoft.littlezeros.client.activities.game.GameSetting;
import com.koolsoft.littlezeros.client.audio.AudioManager;
import com.koolsoft.littlezeros.client.data.LoginManager;
import com.koolsoft.littlezeros.client.resource.BHClientBundleBaseTheme;
import com.koolsoft.littlezeros.client.view.BHImage;
import com.koolsoft.littlezeros.client.view.KSCheckBox;
import com.koolsoft.littlezeros.client.view.dictionary.ItemWord;
import com.koolsoft.littlezeros.shared.Config;
import com.koolsoft.littlezeros.shared.Face;

public class QuizAnswerCardTest extends KSCheckBox {
	
	private boolean correctAnswer = false;
	protected Face face = null;
	private GameSetting gameSetting;
	private int imageMaxSize = 0;
	protected ItemWord itemWord = new ItemWord();
	HorizontalPanel nameHtml = new HorizontalPanel();
	
	public QuizAnswerCardTest(Face face, GameSetting gameSetting) {
		super(BHClientBundleBaseTheme.IMPL.getBHMGWTClientBundle().noneAnswer(), null, 20);
		this.setSpacing(0);
		this.titleHTML = new HTML();
		this.face = face;
		this.gameSetting = gameSetting;
		setStyleCard();
		showFace();
	}

	public boolean isCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(boolean correctAnswer) {
		this.correctAnswer = correctAnswer;
		if(correctAnswer){
			showAnswerForTester();
		}
	}
	
	public void showAnswerForTester(){
		if(LoginManager.getCurrentUser().isTester()){
			this.getContentPanel().getElement().getStyle().setProperty("borderBottom","1px solid red");
		}
	}
	
	public void setFontSize(double fontSize){
		this.getTitleHTML().getElement().getStyle().setFontSize(fontSize, Unit.EM);
		itemWord.getElement().getStyle().setFontSize(fontSize, Unit.EM);
	}
	
	public void setStyleCard(){
		this.setWidth(this.gameSetting.getWidth() + "px");
		this.setStyleName(CssToken.CARD_ANSWER , true);
		this.setCellWidth(imageCheckBox, "25px");
		this.getElement().getStyle().setProperty("margin", "5px auto");
		this.setHeight("100%");
		this.getElement().getStyle().setPaddingLeft(5, Unit.PX);
		this.getElement().getStyle().setPaddingRight(5, Unit.PX);
		if(LoginManager.getUserSetting() != null) {
			this.getElement().getStyle().setBackgroundColor(LoginManager.getUserSetting().getCardBackground());
			this.getElement().getStyle().setColor(LoginManager.getUserSetting().getCardColor());
		}
		this.getTitleHTML().getElement().getStyle().setTextAlign(TextAlign.JUSTIFY);
		this.getTitleHTML().getElement().getStyle().setPaddingTop(15, Unit.PX);
		this.getTitleHTML().getElement().getStyle().setPaddingBottom(15, Unit.PX);
		this.itemWord.getElement().getStyle().setPaddingTop(15, Unit.PX);
		this.itemWord.getElement().getStyle().setPaddingBottom(15, Unit.PX);
		this.getElement().getStyle().setPosition(Position.RELATIVE);
		setCardBorder(true);
	}
	
	public void setCardBorder(boolean isActive){
		this.getContentPanel().getElement().getStyle().setProperty("borderBottom", !isActive ? "none" : "1px solid #e3f2fd");
	}
	
	protected void showFace() {
		contentPanel.clear();
		contentPanel.add(titleHTML);
		if (face.getFaceType() == Config.FACE_FRONT) {
			ClientUtils.adminLog("showFacexxxx 1");
			if(face.hasSound()) {
				if(!face.getText().isEmpty()) {
					this.setTitle(face.getText());
				}
 				else if(face.hasImage()) {
 					ClientUtils.adminLog("showFacexxxx 2");
 					imageMaxSize = this.gameSetting.getImageWidth() > 0 ? this.gameSetting.getImageWidth() : 80;
 					BHImage image = new BHImage(imageMaxSize, imageMaxSize - 5);
 					image.setUrl(face.getImage());
 					image.load(face.getImage());
 					image.getElement().getStyle().setFloat(Float.LEFT);
 					this.getContentPanel().insert(image, 0);
 					getContentPanel().setCellWidth(image, imageMaxSize + "px");
 				}
				
			} else {
				if(face.hasImage()) {
					ClientUtils.adminLog("showFacexxxx 3");
 					imageMaxSize = this.gameSetting.getImageWidth() > 0 ? this.gameSetting.getImageWidth() : 80;
					BHImage image = new BHImage(imageMaxSize, imageMaxSize - 5);
					image.setUrl(face.getImage());
					image.load(face.getImage());
					image.getElement().getStyle().setFloat(Float.LEFT);
					this.getContentPanel().insert(image, 0);
					getContentPanel().setCellWidth(image, imageMaxSize + "px");
				}
				if(!face.getText().isEmpty()) {
					ClientUtils.adminLog("showFacexxxx 4");
					this.setTitle(face.getText());
				}
			}
		}
		else {
			if (face.hasImage()) {
				ClientUtils.adminLog("showFacexxxx 5");
				imageMaxSize = 80;
				//genTextWithImageFace();
				BHImage image = new BHImage(imageMaxSize, imageMaxSize - 5);
				//image.setUrl(face.getImage());
				//image.load(face.getImage());
				image.getElement().getStyle().setFloat(Float.LEFT);
				this.getContentPanel().insert(image, 0);
				getContentPanel().setCellWidth(image, imageMaxSize + "px");
				ClientUtils.checkAndLoadImage(face.getImage(), image);
			} 
			if(!face.getText().isEmpty()) {
				ClientUtils.adminLog("showFacexxxx 6");
				this.setTitle(face.getText());
			}
		}
//		ClientUtils.log("Quiz answer height: "+ this.getGameSetting().getHeight());
		if(this.getGameSetting().getHeight()>50 && !face.hasImage()) {
			this.getGameSetting().setImageSize(50, 50);
			this.setHeight(imageMaxSize + "px");
		}
		getContentPanel().setCellWidth(titleHTML, this.gameSetting.getWidth() - imageMaxSize - 40 + "px");
	}

	public Face getFace() {
		return this.face;
	}

	public void playSound() {
		AudioManager.createAudioPlayer(face, null).play(face.getText(), face.getLangCode(), null);
//		if(this.face.hasSound())
//			LittleZeros.getAudioPlayer().play(face.getSound());
	}

	/**
	 * @return the gameSetting
	 */
	public GameSetting getGameSetting() {
		return gameSetting;
	}

	/**
	 * @param b
	 */
	public void updateStyle(boolean isCorrect) {
		if(isCorrect) {
			setStyleName(CssToken.CARD_ANSWER_CORRECT, true);
//			setStyleName(CssToken.PULSE, true);
//			getElement().getStyle().setProperty("color", GameCore.CORRECT_COLOR + " !important");
			setImageCheck(BHClientBundleBaseTheme.IMPL.getBHMGWTClientBundle().correctAnswer());
		} else {
			setStyleName(CssToken.CARD_ANSWER_INCORRECT, true);
//			getElement().getStyle().setProperty("color", GameCore.INCORRECT_COLOR + " !important");
			setImageCheck(BHClientBundleBaseTheme.IMPL.getBHMGWTClientBundle().incorrectAnswer());
		}
		setCardBorder(false);
		this.setEnableCheckBox(false);
	}

	public void showUserAnswer(String userId, String playerAnswer,
			boolean result) {
		//TODO
		updateStyle(result);
		int index = this.getWidgetCount();
		HTML playerAnswerHtml = new HTML(playerAnswer);
		playerAnswerHtml.getElement().getStyle().setFontSize(0.6, Unit.EM);
		playerAnswerHtml.getElement().getStyle().setPaddingLeft(3, Unit.PX);
		nameHtml.getElement().getStyle().setPosition(Position.ABSOLUTE);
		nameHtml.getElement().getStyle().setRight(10, Unit.PX);
		nameHtml.getElement().getStyle().setBottom(2, Unit.PX);
		nameHtml.add(playerAnswerHtml);
		this.insert(nameHtml, index);
	}
	
	public void showTextWithDictionary(){
		this.showTextWithDictionary(titleHTML.getText());
	}
	
	public void showTextWithDictionary(String text){
		itemWord.showText(text, false, DictionaryType.OUT);
		contentPanel.clear();
		contentPanel.add(itemWord);
	}
	
	public void refreshView(boolean dictionaryMode) {
		this.removeStyleName(CssToken.CARD_ANSWER);
		if (dictionaryMode) {
			showTextWithDictionary();
		}
		else {
			this.setStyleName(CssToken.CARD_ANSWER , true);
			showFace();
		}
	}
}