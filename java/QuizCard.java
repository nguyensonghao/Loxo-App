package com.koolsoft.littlezeros.client.activities.game.quiz;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.koolsoft.littlezeros.client.ClientUtils;
import com.koolsoft.littlezeros.client.CssToken;
import com.koolsoft.littlezeros.client.activities.game.GameSetting;
import com.koolsoft.littlezeros.client.activities.game.widget.TouchCard;
import com.koolsoft.littlezeros.client.audio.AudioManager;
import com.koolsoft.littlezeros.client.data.LoginManager;
import com.koolsoft.littlezeros.client.localization.Locale;
import com.koolsoft.littlezeros.client.view.AlertDialogUiView;
import com.koolsoft.littlezeros.client.view.BHImage;
import com.koolsoft.littlezeros.client.view.KSDialogOverlay;
import com.koolsoft.littlezeros.shared.Config;
import com.koolsoft.littlezeros.shared.Face;

public class QuizCard extends TouchCard {
	
	public QuizCard(Face face, GameSetting gameSetting) {
		this(face, gameSetting, false);
		questionIcon.setVisible(false);
	}
	
	public QuizCard(Face face, GameSetting gameSetting, boolean isPreview) {
		super(face, gameSetting,isPreview);
		this.setStyleName(CssToken.CARD_QUESTION, true);
		this.removeStyleName(CssToken.CARDVIEWER);
		showMoreButton.getElement().getStyle().setProperty("paddingRight", "20px");
		showMoreButton.removeFromParent();
		addTapHandler();
		if(LoginManager.getUserSetting() != null) {
			this.getElement().getStyle().setBackgroundColor(LoginManager.getUserSetting().getCardBackground());
			this.getElement().getStyle().setColor(LoginManager.getUserSetting().getCardColor());
		}
		questionIcon.setVisible(false);
	}
	
	@Override
	protected void genSoundFaceCorner() {
		super.genSoundFaceCorner();
		soundButton.getElement().getStyle().setBottom(-17, Unit.PX);
		soundButton.getElement().getStyle().setRight(-10, Unit.PX);
		soundButton.getElement().getStyle().setZIndex(10);
	}
	
	protected void addTapHandler() {
		if (soundButton != null) {
			soundButton.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent event) {
					playSound(true, !preview);
				}
			});
		}
		if(image != null && image.getParent() != null){
			image.addTapHandler(new TapHandler() {
				@Override
				public void onTap(TapEvent event) {
					if (image.getParent() != null) {
						previewImage();
					}
				}
			});
		}
//		else {
//			tapHandler = this.asWidget().addTapHandler(new TapHandler() {
//			@Override
//			public void onTap(TapEvent event) {
////				if(event.getTargetElement().getClassName().contains(CssToken.AUDIO_PLAYER_PANEL)
////						|| event.getTargetElement().getClassName().contains(CssToken.AUDIO_PLAYER_PLAYBUTTON))
////					return;
////				else if (textArea.getParent() != null
////						|| (soundButton.getParent() != null && !soundButton.isActive())) {
////					playSound(true, !preview);
////				}
//////				else {
//				previewContent();
////				}
//				}
//			});
//		}
	}
	
	protected void previewContent() {
		GameSetting gameSetting = new GameSetting(true, ClientUtils.getScreenWidth(), ClientUtils.getScreenHeight());
		QuizCard preview = new QuizCard(face, gameSetting, true);
		VerticalPanel contentPanel = new VerticalPanel();
		contentPanel.add(preview);
		new AlertDialogUiView(Locale.get().question(), contentPanel, true).show();
	}
	
	@Override
	protected void showFace() {
		if (!LoginManager.getUserSetting().isGameWithSound() && !face.isGameWithSound() && face.hasImage() && face.hasText()) {
			genTextWithImageFace();
		}
		else {
			super.showFace();
		}
		KSDialogOverlay.hideAll();
	}
	
	@Override
	protected void genSoundFace() {
		if (face.isTts()) {
			soundButton.getElement().getStyle().setPadding(10, Unit.PX);
			this.add(this.soundButton);
			if ((image != null && image.getUrl() != null && !image.getUrl().isEmpty()) || (textArea.getText() != null && !textArea.getText().isEmpty())) {
				genSoundFaceCorner();
			}
			else {
				this.setCellVerticalAlignment(this.soundButton, HasVerticalAlignment.ALIGN_MIDDLE);
				this.setCellHorizontalAlignment(this.soundButton, HasHorizontalAlignment.ALIGN_CENTER);
			}
		}
		else {
			//Widget widget = getAudioPlayer().isWidget();
			audioPlayer = AudioManager.createAudioPlayer(face, null);
			audioPlayer.setLoadingTime(Config.TIME_WAIT_AUDIO_DEFAULT);
			this.insert(audioPlayer.isWidget(), 0);
			playSound(true, !preview);
		}
		getAudioPlayer().setAudioUrl(face.getSound());
		//if (face.isGameWithSound())
		if (face.hasSound() && face.isTts() && LoginManager.getUserSetting().isGameWithSound()) {
			playSound(true, !preview);			
		}
	}
	
	@Override
	public void adjustFontSize() {
		
	}
	
	@Override
	protected void genImageFace() {
		boolean loadImage = (image == null 
				|| !image.getUrl().contains(face.getImage()));
		//if (image == null) {
			image = new BHImage(setting.getImageWidth(), setting.getImageHeight());
		//} 
		image.removeFromParent();
		this.add(image);
		this.setCellHorizontalAlignment(image, HasHorizontalAlignment.ALIGN_CENTER);
		//setCurrentWidget(image);
		if (loadImage) { // only load if authorAvatar has not been loaded yet.
			//TODO
			ClientUtils.checkAndLoadImage(face.getImage(), image);
		}
	}
}