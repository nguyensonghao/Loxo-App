package com.koolsoft.littlezeros.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.koolsoft.littlezeros.client.ClientUtils;

public class Face implements IsSerializable, Serializable {

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;
	private String text = Config.TEXT_EMPTY;
	private String image = Config.TEXT_EMPTY;
	private String sound = Config.TEXT_EMPTY;
	private String video = Config.TEXT_EMPTY;
	private String hint = Config.TEXT_EMPTY;
	private String partOfSpeech = Config.TEXT_EMPTY;
	private long cardId = Config.NULL_ID;
	private int langCode = 16;
	private int faceType = Config.FACE_FRONT;
	private int gameType = 0;
	private boolean isTts = false;
	private int gameWithSound = 1;
	private int gameWithSpeech = 1;
	
	public Face() {}
	
	public Face(int faceType, int gameType, long cardId, String text, String image, String sound, int langCode, String partOfSpeech) {
		this();
		this.faceType = faceType;
		this.gameType = gameType;
		this.cardId = cardId;
		this.text = text;
		this.image = image;
		this.sound = sound;
		this.partOfSpeech = partOfSpeech;
		if (langCode <= 0)
			langCode = AppConfig.LANGUAGE_ID;
		this.setLangCode(langCode);
		this.setTts(isTts);
		//ClientUtils.log("Face ****************** ");
	}
	
	public Face(int faceType, int gameType, long cardId, String text, String image, String sound, String hint, String partOfSpeech) {
		this(faceType, gameType, cardId, text, image, sound, AppConfig.LANGUAGE_ID, partOfSpeech);
		setHint(hint);
	}
	
	public String getPartOfSpeech() {
		return partOfSpeech;
	}
	
	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String content) {
		this.text = content;
	}	

	public void setCardId(long cardid) {
		this.cardId = cardid;
	}

	public long getCardId() {
		return cardId;
	}

	public String getImage() {
		if (image == null || image.isEmpty()) {
			if (this.text != null && this.text.contains("data:authorAvatar/")) {
				this.image = this.text;
			}
		}
		return image;
	}

	public void setImage(String imageUrl) {
		this.image = imageUrl;
		if (this.image == null)
			imageUrl = "";
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String soundUrl) {
		this.sound = soundUrl;
		if (this.sound == null)
			this.sound = "";
	}
	
	public void setSound(String soundUrl, boolean isTts) {
		this.setSound(soundUrl);
		this.setTts(isTts);
	}

	public int getLangCode() {
		return langCode;
	}

	public void setLangCode(int langCode) {
		this.langCode = langCode;
	}

	public int getFaceType() {
		return faceType;
	}
	
	public void setFaceType(int faceType) {
		this.faceType = faceType;
	}

	public String getVideoUrl() {
		return video;
	}
	public void setVideoUrl(String videoUrl) {
		this.video = videoUrl;
	}
	
	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getHint() {
		return hint;
	}
	
	public boolean isTts() {
		return isTts;
	}

	public void setTts(boolean isTts) {
		this.isTts = isTts;
	}
	
	public void setHint(String hint) {
		hint = ClientUtils.getImageContent(hint, true);
		ClientUtils.adminLog("hint : " + hint);
		this.hint = hint;
	}
	public boolean hasText() {
		return text != null && !text.isEmpty();
	}
	public boolean hasImage() {
		return image != null && !image.isEmpty();
	}
	public boolean hasSound() {
        return sound != null && !sound.isEmpty();
	}
	public boolean isEmpty() {
		return (this.text == null || this.text.isEmpty())
				&& (this.image == null || this.image.isEmpty());
	}

//	public void copy(Face face) {
//		this.cardId = face.cardId;
//		this.text = face.text;
//		this.image = face.image;
//		this.langCode = face.langCode;
//		this.sound = face.sound;
//		this.faceType = face.faceType;
//	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(int gameType) {
		this.gameType = gameType;
	}

	public boolean hasHint() {
		boolean hasHint =  hint != null && !hint.isEmpty();
		return hasHint;
	}
	
	
//	public boolean isGameWithSound() {
//		return gameType == Config.GAME_QUIZ_SOUND
//				|| gameType == Config.GAME_QUIZ_SOUND_REVERSE
//				|| gameType == Config.GAME_SPELLING_SOUND
//				|| gameType == Config.GAME_SPELLING_TYPING_SOUND
//				|| gameType == Config.GAME_MATCHING_SOUND
//				|| gameType == Config.GAME_MEMORIZE_SOUND;
//	}

	public boolean isGameWithSound() {
		return this.gameWithSound == 1;
	}

	public boolean isGameWithSpeech() {
		return this.gameWithSpeech == 1;
	}
	
	public int getGameWithSound() {
		return gameWithSound;
	}

	public void setGameWithSound(int gameWithSound) {
		this.gameWithSound = gameWithSound;
	}
	
	public void setGameWithSound(boolean gameWithSound) {
		this.gameWithSound = gameWithSound ? 1 : 0;
	}

	public int getGameWithSpeech() {
		return gameWithSpeech;
	}

	public void setGameWithSpeech(int gameWithSpeech) {
		this.gameWithSpeech = gameWithSpeech;
	}
	
	public void setGameWithSpeech(boolean gameWithSpeech) {
		this.gameWithSpeech = gameWithSpeech ? 1 : 0;
	}

	public boolean isSpellingGame() {
		return gameType == Config.GAME_SPELLING
				//|| gameType == Config.GAME_SPELLING_SOUND
				|| gameType == Config.GAME_SPELLING_TYPING;
				//|| gameType == Config.GAME_SPELLING_TYPING_SOUND;
	}
}