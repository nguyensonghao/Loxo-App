package com.koolsoft.littlezeros.client.activities.game.quiz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.koolsoft.littlezeros.client.ClientData;
import com.koolsoft.littlezeros.client.ClientUtils;
import com.koolsoft.littlezeros.client.activities.game.gamecore.GameObject;
import com.koolsoft.littlezeros.client.activities.game.study.StudyObject;
import com.koolsoft.littlezeros.client.data.FileManager;
import com.koolsoft.littlezeros.client.data.LoginManager;
import com.koolsoft.littlezeros.client.view.Toaster;
import com.koolsoft.littlezeros.shared.AppConfig;
import com.koolsoft.littlezeros.shared.Config;
import com.koolsoft.littlezeros.shared.Face;
import com.koolsoft.littlezeros.shared.model.Card;
import com.koolsoft.littlezeros.shared.model.CardProgress;
import com.koolsoft.littlezeros.shared.model.CardStatistic;

public class QuizGameObject implements GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long parentId = Config.NULL_ID;
	private Long topicId = Config.NULL_ID;
	private Long categoryId = Config.NULL_ID;
	private Face question = null;
	private List<Face> answers = new ArrayList<Face>();
	private int gameObjectType = Config.GAME_QUIZ;
	private int incorrectScore = 0;
	private int correctScore = 0;
	private int box = 0;
	private int level = 0;
	private CardProgress progress = null;
	private String topicName = "";
	private int index;
	private int numberCorrectAnswer = 0;
	private int numberAnswer = 0;
	private int gameSkill = 0;
	private boolean isLoadedData = false;
	private int database;
	private CardStatistic cardStatistic = null;
	private List<Long> cardIds = new ArrayList<Long>();
	private List<StudyObject> studyObjects = new ArrayList<StudyObject>();
	
	public QuizGameObject() {
	}
	
	
	/**
	 * Create quiz game object that has more than one key answer stored in backtexts
	 * @param card
	 * @param topicId
	 */
	public QuizGameObject(Card card, Long topicId, int userLevel) {
		this.setParentId(card.getTopicId());
		testCard(card);
		this.categoryId = card.getParentId();
		this.database = card.getDatabase();
		this.cardStatistic = card.getCardStatistic();
		int box = 0;
		if(card.getProgress() != null){
			box = card.getProgress().getBoxNum();
		}
		this.setBox(box);
		question = new Face(Config.FACE_FRONT, Config.GAME_QUIZ, card.getId(), 
				ClientUtils.getImageContent(card.getFrontText(), true), "", "", card.getFrontLanguage(), card.getTypeOfWord());
		if(!card.getFrontImage().isEmpty())
			question.setImage(ClientUtils.getImageContent(card.getFrontImage(),false));
		if (question.isTts() && ClientUtils.split(question.getSound()).size() > Config.MAX_WORD_GAME_WITH_SOUND) {
			question.setGameWithSound(false);
		}
		else {
			question.setGameWithSound(this.isGameWithSound(card) && userLevel > 2 && card.getDatabase() == Config.VOCABULARY_DATABASE 
					&& Config.isListeningSkillSupported());
		}
		question.setSound(ClientUtils.getSoundContent(card.getFrontSound()));
		question.setHint(card.getFrontHint());
		question.setCardId(card.getId());
		if(card.getBackText() != null && !card.getBackText().isEmpty()) {
			Face answerFace = new Face(Config.FACE_BACK, Config.GAME_QUIZ, card.getId(), 
					ClientUtils.getImageContent(card.getBackText(), true), "", "", card.getBackLanguage(), card.getTypeOfWord());
			answerFace.setCardId(card.getId());
			this.getAnswers().add(answerFace);
			numberCorrectAnswer++;
			isLoadedData = true;
		} else if(card.getBackTexts() != null && !card.getBackTexts().isEmpty()) {
			for(String s : card.getBackTexts()) {
				if(s.isEmpty())
					continue;
				else {
					Face answerFace = new Face(Config.FACE_BACK, Config.GAME_QUIZ, card.getId(), 
							ClientUtils.getImageContent(s, true), "", "", card.getBackLanguage(), card.getTypeOfWord());
					answerFace.setCardId(card.getId());
					this.getAnswers().add(answerFace);
					numberCorrectAnswer++;
				}
			}
			if(card.getMultiChoices() == null || card.getMultiChoices().isEmpty()){
				int fakeNum = getFakeNum(userLevel);
				genFakeAnswers(card, fakeNum, Config.GAME_QUIZ);
			} else {
				isLoadedData = true;
			}
		} else {
			isLoadedData = true;
		}
		this.setId(card.getId());
		if(card.getMultiChoices()!= null && !card.getMultiChoices().isEmpty()) {
			for (String s : card.getMultiChoices()) {
				if(s.isEmpty())
					continue;
				else {
					ClientUtils.log("getMultiChoices : " + ClientUtils.getImageContent(s, true));
					Face fakeFace = new Face(Config.FACE_BACK, Config.GAME_QUIZ, Config.NULL_ID, 
							ClientUtils.getImageContent(s, true), "", "", card.getBackLanguage(), card.getTypeOfWord());
					this.getAnswers().add(fakeFace);
				}
			}
		}
		this.setLevel(card.getDifficultyLevel());
		createOrCopyCardProgress(card);
		this.topicName = card.getTopicName();
		Collections.sort(this.getAnswers(), new Comparator<Face>() {

			@Override
			public int compare(Face o1, Face o2) {
				return o1.getText().compareTo(o2.getText());
			}
		});
	}
	
	private void testCard(Card card){
//		for (int i = 0; i < 5; i++) {
//			card.setFrontHint(card.getFrontHint() + " " + card.getFrontHint());
//		}
	}
	
	/**
	 * Create quiz game object for Challenge
	 * @param card
	 * @param topicId
	 * @param answerMap
	 */
	//TODO
	public QuizGameObject(Card card, Long topicId, Map<Integer, String> answerMap) {
		this.setParentId(card.getTopicId());
		testCard(card);
		this.categoryId = card.getParentId();
		this.database = card.getDatabase();
		this.cardStatistic = card.getCardStatistic();
		int box = 0;
		if(card.getProgress() != null){
			box = card.getProgress().getBoxNum();
		}
		this.setBox(box);
		question = new Face(Config.FACE_FRONT, Config.GAME_QUIZ, card.getId(), 
				ClientUtils.getImageContent(card.getFrontText(), true), "", "", card.getFrontLanguage(), card.getTypeOfWord());
		if(!card.getFrontImage().isEmpty())
			question.setImage(ClientUtils.getImageContent(card.getFrontImage(),false));
		question.setGameWithSound(false);
		question.setSound(ClientUtils.getSoundContent(card.getFrontSound()));
		question.setHint(card.getFrontHint());
		question.setCardId(card.getId());
		this.setId(card.getId());
		this.setLevel(card.getDifficultyLevel());
		createOrCopyCardProgress(card);
		this.topicName = card.getTopicName();
		
		List<Integer> keys = new ArrayList<Integer>(answerMap.keySet());
		Collections.sort(keys, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		
		for(Integer key : keys) {
			String answer = answerMap.get(key);
			Face answerFace = new Face(Config.FACE_BACK, Config.GAME_QUIZ, Config.NULL_ID, 
					ClientUtils.getImageContent(answer, true), "", "", card.getBackLanguage(), card.getTypeOfWord());
			if(answer.equalsIgnoreCase(card.getBackText())) {
				answerFace.setCardId(card.getId());
				numberCorrectAnswer++;
			} 
			this.getAnswers().add(answerFace);
		}
		isLoadedData = true;
	}
	
	/**
	 * Create quiz object that already has a list of multi-choices pre-made
	 * @param card
	 * @param gameType
	 * @param getHint
	 */
	//TODO
	public QuizGameObject(Card card, int gameType, boolean getHint, int userLevel) {
		this();
		testCard(card);
		this.setParentId(card.getParentId());
		this.setTopicName(card.getTopicName());
		this.categoryId = card.getParentId();
		this.database = card.getDatabase();
		this.cardStatistic = card.getCardStatistic();
		int box = 0;
		if(card.getProgress() != null){
			box = card.getProgress().getBoxNum();
		}
		this.setBox(box);
		Face answerFace = null;
		String fronText = ClientUtils.getImageContent(card.getFrontText(),true);
//		if (gameType == Config.GAME_QUIZ_REVERSE) {// || gameType == Config.GAME_QUIZ_SOUND_REVERSE) {
//			question = new Face(Config.FACE_FRONT, gameType, card.getId(), ClientUtils.getImageContent(card.getBackText(),true), 
//					card.getBackImage(), card.getBackSound(), card.getBackLanguage());
//			answerFace = new Face(Config.FACE_BACK, gameType, card.getId(), ClientUtils.getImageContent(card.getFrontText(),true), 
//					card.getFrontImage(), card.getFrontSound(), card.getFrontLanguage());
//		}
//		else {
		question = new Face(Config.FACE_FRONT, gameType, card.getId(), fronText, 
				card.getFrontImage(), card.getFrontSound(), card.getFrontLanguage(), card.getTypeOfWord());
		answerFace = new Face(Config.FACE_BACK, gameType, card.getId(), ClientUtils.getImageContent(card.getBackText(),true), 
				card.getBackImage(), card.getBackSound(), card.getBackLanguage(), card.getTypeOfWord());
//		}
		if (question.isTts() && ClientUtils.split(question.getSound()).size() > Config.MAX_WORD_GAME_WITH_SOUND) {
			question.setGameWithSound(false);
		}
		else {
			question.setGameWithSound(this.isGameWithSound(card) && userLevel > 2 && card.getDatabase() == Config.VOCABULARY_DATABASE 
					&& Config.isListeningSkillSupported());
		}
		FileManager.getImagePath(question.getImage(), question);
		FileManager.getImagePath(answerFace.getImage(), answerFace);
		if (Config.isListeningSkillSupported() || !card.getFrontText().contains("...")) {
			FileManager.getSoundPath(question.getSound(), question, fronText, card.getFrontLanguage());
			FileManager.getSoundPath(answerFace.getSound(), answerFace, fronText, card.getFrontLanguage());
		}
		if(getHint) {
			question.setHint(card.getFrontHint());
			answerFace.setHint(card.getBackHint());
		}
		this.getAnswers().add(answerFace);
		this.setId(card.getId());
		if(card.getMultiChoices()!= null && !card.getMultiChoices().isEmpty()) {
			for (String s : card.getMultiChoices()) {
				ClientUtils.log("getMultiChoices 2: " + ClientUtils.getImageContent(s, true));
				Face fakeFace = new Face(Config.FACE_BACK, gameType, Config.NULL_ID, ClientUtils.getImageContent(s, true), "", "", answerFace.getLangCode(), card.getTypeOfWord());
				this.getAnswers().add(fakeFace);
			}
		} 
		Collections.sort(this.getAnswers(), new Comparator<Face>() {

			@Override
			public int compare(Face o1, Face o2) {
				return o1.getText().compareTo(o2.getText());
			}
		});
		createOrCopyCardProgress(card);
		isLoadedData = true;
	}
	
	public QuizGameObject(final Card card, int quizType, int progress, int gameMode, int userLevel) {
		this();
		testCard(card);
		boolean reverse = card.getDatabase() == Config.VOCABULARY_DATABASE
				&& Config.isListeningSkillSupported() && ClientUtils.nextInt(5) == 3;
		if (reverse && gameMode != Config.MODE_CHALLENGE) {
			quizType = Config.GAME_QUIZ_REVERSE;
		}
		this.setParentId(card.getParentId());
		this.setId(card.getId());
		int box = 0;
		if(card.getProgress() != null){
			box = card.getProgress().getBoxNum();
		}
		this.setBox(box);
		this.setLevel(card.getDifficultyLevel());
		this.categoryId = card.getParentId();
		this.database = card.getDatabase();
		this.cardStatistic = card.getCardStatistic();
		CardProgress cardProgress = card.getProgress();
		if (cardProgress == null) {
			String id = AppConfig.MAIN_TOPIC_ID + "_" +card.getId() + "_" + LoginManager.youId();
			cardProgress = new CardProgress(id,card.getId(), card.getTopicId());
			cardProgress.setUserId(LoginManager.getCurrentUser().getId());
			cardProgress.setAppId(AppConfig.MAIN_TOPIC_ID);
			this.setProgress(cardProgress);
		} else {
			String id = AppConfig.MAIN_TOPIC_ID + "_" + card.getId() + "_" + LoginManager.youId();
			CardProgress newCardProgress = new CardProgress(id,card.getId(), card.getTopicId());
			newCardProgress.setUserId(LoginManager.getCurrentUser().getId());
			newCardProgress.setHistory(cardProgress.getHistory());
			newCardProgress.setAppId(AppConfig.MAIN_TOPIC_ID);
			this.setProgress(newCardProgress);
		}
		ClientUtils.log("getUserLevelxxx2 : " + card.getFrontText() + " -- " +card.getUserLevel());
		this.setTopicName(card.getTopicName());
		Face[] faces = getFaces(card, quizType);
		this.setQuestion(faces[0]);
		if (gameMode == Config.MODE_CHALLENGE || this.getQuestion().isTts() && ClientUtils.split(this.getQuestion().getSound()).size() > Config.MAX_WORD_GAME_WITH_SOUND) {
			this.getQuestion().setGameWithSound(false);
			if(gameMode == Config.MODE_CHALLENGE){
				this.getQuestion().setSound(Config.TEXT_EMPTY);
			}
		}
		else {
			this.getQuestion().setGameWithSound(this.isGameWithSound(card) && userLevel > 2 && card.getDatabase() == Config.VOCABULARY_DATABASE 
					&& Config.isListeningSkillSupported());
		}
		this.getAnswers().add(faces[1]);
		int fakeNum = 0;
		if(gameMode == Config.MODE_MULTIGAME || gameMode == Config.MODE_TEST || gameMode == Config.MODE_TEST_SCERANIO){
			fakeNum = 3;
		} else {
			fakeNum = getFakeNum(userLevel);
		}
//		final int fakeNum = gameMode == Config.MODE_MULTIGAME 
//				? 3 : (gameMode == Config.MODE_TEST || gameMode == Config.MODE_TEST_SCERANIO) 
//						? 3 : userLevel == 1 ? 3 : userLevel == 2 ? 4 : 5 ;
		//getRelated cards
		final int fQuizType = quizType;
		//TODO
		genFakeAnswers(card, fakeNum, fQuizType);
		createOrCopyCardProgress(card);
	}
	
	private int getFakeNum(int level){
		int fakeNum = 3;
		if(level == 1){
			fakeNum = 3;
		}
		if(level == 2 || level == 3){
			fakeNum = 4;
		}
		if(level == 4 || level == 5){
			fakeNum = 5;
		}
		ClientUtils.log("getFakeNum : " + level + " -- " + fakeNum);
		return fakeNum;
	}
	
	private void genFakeAnswers(final Card card, final int fakeNum, final int fQuizType) {
		if(card.getDatabase() <= Config.NOT_DATABASE){
			return;
		}
		ClientData.getCardManager(card.getDatabase()).getCards(card.getParentId(), fakeNum + 2, new AsyncCallback<List<Card>>() {
			@Override
			public void onFailure(Throwable caught) {
				Toaster.showToast("Failed to get related cards");
				isLoadedData = true;
			}
			@Override
			public void onSuccess(List<Card> result) {
				ClientUtils.log("QuizGameObject getFake Cards : " + result.size());
				List<Card> ramdomCards = new ArrayList<Card>();
				for (Card fCard : result) {
					if(ramdomCards.size() <= fakeNum + 1 
							&& (getAnswers().get(0).getText().isEmpty() 
							|| fCard.getBackTextContent().isEmpty()
							|| !getAnswers().get(0).getText().equals(fCard.getBackTextContent()))) {
						ramdomCards.add(fCard);
					}
				}
				List<String> answerKeys = new ArrayList<String>();
				for (Card fCard : ramdomCards) {
					Face[] fakeFaces = getFaces(fCard, fQuizType);
					if (fakeFaces[1].getCardId() != question.getCardId() 
							&& !answerKeys.contains(fakeFaces[1])) {
						ClientUtils.log("QuizFakeOptions: " + fakeFaces[1].getText() + ":" 
							+ fakeFaces[1].getImage() + ":" + fakeFaces[1].getSound());
						getAnswers().add(fakeFaces[1]);
						answerKeys.add(fakeFaces[1].getText());
						cardIds.add(fCard.getId());
					}
					if (getAnswers().size() >= fakeNum) {
						List<Face> suffers = new ArrayList<Face>();
						while (!getAnswers().isEmpty()) {
							Face face = getAnswers().remove(ClientUtils.nextInt(getAnswers().size()));
							suffers.add(face);
						}
						setAnswers(suffers);
						break;
					}
				}
				isLoadedData = true;
			}
		});
	}
	
	public boolean isGameWithSound(Card card) {
		boolean isGameWithSound = card.hasFrontSound() || LoginManager.getUserSetting().isGameWithSound() && ClientUtils.nextBool();
		this.gameSkill = card.hasFrontSound() ? Config.SKILL_LISTENING : Config.SKILL_READING;
		return isGameWithSound;
	}
	
	private void createOrCopyCardProgress(Card card) {
		CardProgress cardProgress = card.getProgress();
		if (cardProgress == null) {
			String id = AppConfig.MAIN_TOPIC_ID + "_" + card.getId() + "_" + LoginManager.youId();
			cardProgress = new CardProgress(id,card.getId(), card.getTopicId());
			cardProgress.setUserId(LoginManager.getCurrentUser().getId());
			cardProgress.setAppId(AppConfig.MAIN_TOPIC_ID);
			this.progress = cardProgress;
			StudyObject studyObject = new StudyObject(card, topicId);
			studyObjects.add(studyObject);
		} else {
			String id = AppConfig.MAIN_TOPIC_ID + "_" + card.getId() + "_" + LoginManager.youId();
			CardProgress newCardProgress = new CardProgress(id,card.getId(), card.getTopicId());
			newCardProgress.setUserId(LoginManager.getCurrentUser().getId());
			newCardProgress.setHistory(cardProgress.getHistory());
			newCardProgress.setAppId(AppConfig.MAIN_TOPIC_ID);
			this.progress = newCardProgress;
		}
		ClientUtils.log("copy or create cardprogress: " + (this.progress != null ? " not null " : " null") + " - " + this.id);
	}
	
	private Face[] getFaces(Card card, int quizType) {
		Face questionFace = null;
		Face answerFace = null; //new Face(question.getNativeLanguage());
		final Map<String, Integer> answerMap = new HashMap<String, Integer>();
		ClientUtils.log("Of card: " + card.getFrontText() + ":" + card.getBackText());
		String frontText = ClientUtils.getImageContent(card.getFrontText(), true);
		String backTex = ClientUtils.getImageContent(card.getBackText(), true);
		if (quizType == Config.GAME_QUIZ_REVERSE) { //|| quizType == Config.GAME_QUIZ_SOUND_REVERSE) {
			questionFace = new Face(Config.FACE_FRONT, quizType, card.getId(), backTex, 
					ClientUtils.getImageContent(card.getBackImage(), true), card.getFrontSound(), card.getFrontLanguage(), card.getTypeOfWord());
			answerFace = new Face(Config.FACE_BACK, quizType, card.getId(), frontText, 
					frontText, card.getBackSound(), card.getBackLanguage(), card.getTypeOfWord());
		}
		else {
			questionFace = new Face(Config.FACE_FRONT, quizType, card.getId(), frontText, 
					ClientUtils.getImageContent(card.getFrontImage(), true), card.getFrontSound(), card.getFrontLanguage(), card.getTypeOfWord());
			String answerText = backTex != null && !backTex.isEmpty() ? backTex :
				!card.getBackTexts().isEmpty() ? card.getBackTexts().get(0) : "";
			answerFace = new Face(Config.FACE_BACK, quizType, card.getId(), answerText, 
					ClientUtils.getImageContent(card.getBackImage(), true), card.getBackSound(), card.getBackLanguage(), card.getTypeOfWord());
		}
		ClientUtils.log("Of Quiz: " + questionFace.getText() + ":" + answerFace.getText());
		answerMap.put(answerFace.getText(), 1);
		FileManager.getSoundPath(questionFace.getSound(), questionFace, frontText, card.getFrontLanguage());
		FileManager.getSoundPath(answerFace.getSound(), answerFace, frontText, card.getFrontLanguage());
		if (questionFace.hasImage()) {
			FileManager.getImagePath(questionFace.getImage(), questionFace);
		}
		if (answerFace.hasImage()) {
			FileManager.getImagePath(answerFace.getImage(), answerFace);
		}
		questionFace.setHint(card.getFrontHint());
		answerFace.setHint(card.getBackHint());
		ClientUtils.log("QuizQUestion: " + card.getFrontText() + ":" + card.getFrontImage() + ":" + card.getFrontSound());
		ClientUtils.log("QuizAnswer: " + answerFace.getText() + ":" + answerFace.getImage() + ":" + answerFace.getSound() + "***" + quizType + ":" + Config.getGameName(quizType));
		return new Face[]{questionFace, answerFace};
	}
	
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int getGameObjectType() {
		return gameObjectType;
	}

	public void setGameObjectType(int gameObjectType) {
		this.gameObjectType = gameObjectType;
	}

	public Face getQuestion() {
		return question;
	}

	public void setQuestion(Face question) {
		this.question = question;
	}

	public List<Face> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Face> answers) {
		this.answers = answers;
	}

	@Override
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
	public int getIncorrectScore() {
		return incorrectScore;
	}

	@Override
	public int getCorrectScore() {
		return correctScore;
	}

	@Override
	public void setIncorrectScore(int incorrectScore) {
		this.incorrectScore = incorrectScore;
	}

	@Override
	public void setCorrectScore(int correctScore) {
		this.correctScore = correctScore;
	}

	@Override
	public void setBox(int box) {
		this.box = box;
	}

	@Override
	public int getBox() {
		return box;
	}

	@Override
	public int getStatus() {
		return 1;
	}

	@Override
	public CardProgress getProgress() {
		return progress;
	}

	public void setProgress(CardProgress progress) {
		this.progress = progress;
	}

	@Override
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public void setIndex(int index) {
		this.index = index;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	@Override
	public Long getTopicId() {
		return topicId;
	}

	public int getNumberCorrectAnswer() {
		return numberCorrectAnswer;
	}

	public void setNumberCorrectAnswer(int numberCorrectAnswer) {
		this.numberCorrectAnswer = numberCorrectAnswer;
	}

	public int getNumberAnswer() {
		return numberAnswer;
	}

	public void setNumberAnswer(int numberAnswer) {
		this.numberAnswer = numberAnswer;
	}

	@Override
	public Long getCategoryId() {
		return categoryId;
	}

	@Override
	public int getSkill() {
		return gameSkill;
	}

	@Override
	public boolean isLoadedData() {
		return isLoadedData;
	}


	@Override
	public int getDatabase() {
		return database;
	}


	@Override
	public CardStatistic getCardStatistic() {
		if(cardStatistic == null){
			cardStatistic = ClientData.cardStatistic.getCardStatistic(id);
		}
		return cardStatistic;
	}

	@Override
	public String getAnswerKey() {
		String answerKey = Config.EMPTY_TEXT;
		if (this.getNumberCorrectAnswer() > 1) {
			for (int index=0 ; index < this.getAnswers().size() ; index++) {
				if (this.getAnswers().get(index).getCardId() == this.getQuestion().getCardId()) {
					answerKey += "<br>" + this.getAnswers().get(index).getText();
				}
			}
		}
		else {
			for (Face answer : answers) {
				if (answer.getCardId() == this.getQuestion().getCardId()) {
					answerKey = answer.getText();
					break;
				}
			}
		}
		return answerKey;
	}

	@Override
	public String getAnswerHint() {
		String hint = Config.EMPTY_TEXT;
		for (Face answer : answers) {
			if (answer.getCardId() == this.getQuestion().getCardId()) {
				if (question.getHint() != null  && !question.getHint().isEmpty() && !question.getHint().equals(answer.getText())){
					hint = question.getHint() + "<br>";
				}
				if (answer.getHint() != null && !answer.getHint().isEmpty()) {
					hint += answer.getHint();
				}  
				break;
			}
		}
		return hint;
	}

	@Override
	public List<Long> getCardIds() {
		List<Long> cardIds = new ArrayList<Long>();
		cardIds.add(id);
		for (Long cardId : this.cardIds) {
			cardIds.add(cardId);
		}
		return cardIds;
	}
	
	@Override
	public List<StudyObject> getStudyObject() {
		return studyObjects;
	}


	@Override
	public String getPartOfSpeech() {
		return question.getPartOfSpeech();
	}
}