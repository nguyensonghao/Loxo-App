package com.koolsoft.littlezeros.client.activities.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.dialog.ConfirmDialog.ConfirmCallback;
import com.koolsoft.littlezeros.client.ClientData;
import com.koolsoft.littlezeros.client.ClientUtils;
import com.koolsoft.littlezeros.client.CssToken;
import com.koolsoft.littlezeros.client.LittleZeros;
import com.koolsoft.littlezeros.client.RPCCall;
import com.koolsoft.littlezeros.client.activities.ClientFactory;
import com.koolsoft.littlezeros.client.activities.abouttopic.AboutTopicActivity.Tab;
import com.koolsoft.littlezeros.client.activities.abouttopic.AboutTopicPlace;
import com.koolsoft.littlezeros.client.activities.basic.BasicActivity;
import com.koolsoft.littlezeros.client.activities.basic.BasicPlace;
import com.koolsoft.littlezeros.client.activities.category.CategoryPlace;
import com.koolsoft.littlezeros.client.activities.event.DictionaryTapEvent;
import com.koolsoft.littlezeros.client.activities.event.DictionaryTapEvent.DictionaryType;
import com.koolsoft.littlezeros.client.activities.event.DictionaryTapEventHandler;
import com.koolsoft.littlezeros.client.activities.event.OnAnswerEvent;
import com.koolsoft.littlezeros.client.activities.event.OnAnswerHandler;
import com.koolsoft.littlezeros.client.activities.event.PlayRecordDoneEvent;
import com.koolsoft.littlezeros.client.activities.event.PlayRecordDoneEventHandler;
import com.koolsoft.littlezeros.client.activities.event.PlaySoundDoneEvent;
import com.koolsoft.littlezeros.client.activities.event.PlaySoundDoneEventHandler;
import com.koolsoft.littlezeros.client.activities.event.ReloadGameEvent;
import com.koolsoft.littlezeros.client.activities.event.ReloadGameEventHandler;
import com.koolsoft.littlezeros.client.activities.event.SaveRecordAudioEvent;
import com.koolsoft.littlezeros.client.activities.event.SaveRecordAudioEventHandler;
import com.koolsoft.littlezeros.client.activities.event.TurnonRecordeAudioEvent;
import com.koolsoft.littlezeros.client.activities.event.TurnonRecordeAudioEventHandler;
import com.koolsoft.littlezeros.client.activities.event.UpdateProgressEvent;
import com.koolsoft.littlezeros.client.activities.event.UpdateProgressEventHandler;
import com.koolsoft.littlezeros.client.activities.game.GamePlace.GameSource;
import com.koolsoft.littlezeros.client.activities.game.gamecore.GameObject;
import com.koolsoft.littlezeros.client.activities.game.matching.MatchingGameObject;
import com.koolsoft.littlezeros.client.activities.game.paragraph.ParagraphGameObject;
import com.koolsoft.littlezeros.client.activities.game.quiz.GameObjectResult;
import com.koolsoft.littlezeros.client.activities.game.quiz.QuizGameObject;
import com.koolsoft.littlezeros.client.activities.game.rearrange.RearrangeGameObject;
import com.koolsoft.littlezeros.client.activities.game.result.EndGamePlace;
import com.koolsoft.littlezeros.client.activities.game.speaking.SpeakingGameObject;
import com.koolsoft.littlezeros.client.activities.game.spelling.SpellingGameObject;
import com.koolsoft.littlezeros.client.activities.game.study.StudyObject;
import com.koolsoft.littlezeros.client.activities.game.truefalse.TrueFalseGameObject;
import com.koolsoft.littlezeros.client.activities.game.writing.WritingGameObject;
import com.koolsoft.littlezeros.client.audio.AudioManager;
import com.koolsoft.littlezeros.client.data.FileManager;
import com.koolsoft.littlezeros.client.data.LoginManager;
import com.koolsoft.littlezeros.client.data.ScreenShotManager;
import com.koolsoft.littlezeros.client.datanew.DataManager;
import com.koolsoft.littlezeros.client.facebook.FacebookManager;
import com.koolsoft.littlezeros.client.localization.Locale;
import com.koolsoft.littlezeros.client.plugins.FileTransfer;
import com.koolsoft.littlezeros.client.sliding.SlidingPanel;
import com.koolsoft.littlezeros.client.view.AlertDialogUiView;
import com.koolsoft.littlezeros.client.view.Toaster;
import com.koolsoft.littlezeros.client.view.dictionary.DictionarySearchDialog;
import com.koolsoft.littlezeros.client.views.discussions.CommentSliding;
import com.koolsoft.littlezeros.client.views.discussions.ReplySliding;
import com.koolsoft.littlezeros.shared.AppConfig;
import com.koolsoft.littlezeros.shared.Config;
import com.koolsoft.littlezeros.shared.Face;
import com.koolsoft.littlezeros.shared.model.Card;
import com.koolsoft.littlezeros.shared.model.CardProgress;
import com.koolsoft.littlezeros.shared.model.CardUserStatistic;
import com.koolsoft.littlezeros.shared.model.MyCardData;
import com.koolsoft.littlezeros.shared.model.SystemLog;
import com.koolsoft.littlezeros.shared.model.Topic;
import com.koolsoft.littlezeros.shared.model.TopicProgress;

public class GameActivity extends BasicActivity {
	
	private GameView view;
	protected List<GameObject> currentGameObjects = null;
	protected Topic mainTopic = null;
	protected List<Topic> playTopics = null;
	protected Map<Integer, List<Long>> cardIdMaps = null;
	protected NewBasicGameView newBasicGameView= null;
	protected int gameMode = Config.MODE_PRATICE;
	protected boolean exitGame = false;
	protected Map<Long, Card> playCardsMap = new HashMap<Long, Card>();
	protected Map<Long, Topic> playTopicsMap = new HashMap<Long, Topic>();
	protected int maxQuestion = 0;
	protected Map<String, CardProgress> cardProgressesToUpdate = new HashMap<String, CardProgress>();
	protected Map<Long, CardUserStatistic> cardUserStatisticsToUpdate = new HashMap<Long, CardUserStatistic>();
	private List<Card> cards = null;
	protected int avarageProgress = 0;
	private String imageScreelShot  = "";
	private GameSource gameSource;
	protected static int studyOrder = -1;
	private String error = null;
	
	public GameActivity(ClientFactory clientFactory, Place place) {
		super(clientFactory, place);
		getParamsFromPlace();
	}
	
	protected void getParamsFromPlace() {
		playTopics = null;
		playTopicsMap.clear();
		if(((GamePlace)place).getTopics() !=null) {
			playTopics = ((GamePlace)place).getTopics();
			ClientUtils.log("PlayTopicsSize: " + playTopics.size());
			if(playTopics != null) {
				for(Topic topic : playTopics) {
					ClientUtils.log("PlayTopicIs: " + topic.getName() + ":" + topic.getId());
					playTopicsMap.put(topic.getId(), topic);
					int cardSize = topic.calcTotalCardNum();
					int lastCardSize = 0;
					if(topic.getProgress() != null) {
						lastCardSize = topic.getProgress().getLastChildCardNum();
						if(cardSize > lastCardSize) {
							ClientUtils.log("setPassed : " + topic.getName() + " -- " + cardSize + " -- " + lastCardSize);
							topic.getProgress().setPassed(0);
						}
					}
				}
			}
		}
		mainTopic = ((GamePlace)place).getMainTopic();
		gameMode = ((GamePlace)place).getGameMode();
		maxQuestion = LoginManager.getUserSetting().getNumberQuestion();
		cardIdMaps = ((GamePlace)place).getCardIds();
		cards = ((GamePlace)place).getCards();
		gameSource = ((GamePlace)place).getGameSource();
		studyOrder = ((GamePlace)place).getStudyOrder();
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getGameView();
		if(newBasicGameView == null)
			newBasicGameView = new NewBasicGameView(view.getLayout(),gameMode);
		currentGameObjects = null;
		super.start(panel, eventBus, view);
		view.getContentPanel().add(ClientUtils.getLoadingAnimationHTML());
		panel.setWidget(view);
	}
	
	@Override
	public void onStop() {
		AudioManager.forceStopSound();
	}
	
	@Override 
	protected void bind() {
		super.bind();
		
		addHandlerRegistration(eventBus.addHandler(OnAnswerEvent.TYPE, new OnAnswerHandler() {
			
			@Override
			public void onExcute(OnAnswerEvent onAnswerEvent) {
				ClientData.DISCUSION_MANAGER.getDisscussionNumber(onAnswerEvent.getCardId(), new AsyncCallback<Integer>() {
					
					@Override
					public void onSuccess(Integer result) {
						newBasicGameView.getCurrentGameCore().getGameCoreView().getResultPanel().setNumberComment(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						newBasicGameView.getCurrentGameCore().getGameCoreView().getResultPanel().setNumberComment(0);
					}
				});
			}
		}));
		
		
		addHandlerRegistration(eventBus.addHandler(UpdateProgressEvent.TYPE, new UpdateProgressEventHandler() {
			@Override
			public void onUpdate(UpdateProgressEvent event) {
				if(event.getType() == UpdateProgressEvent.TYPE_UPDATE_CARD) {
					updateCardProgress(event.getCardId(), event.getCorrect(), event.getGameType(), event.getGameTime(), event.getAnswerIndex(), event.getSkill());
				} else if(event.getType() == UpdateProgressEvent.TYPE_UPDATE_CARDS) {
					updateCardProgress(event.getCardIds(), event.getCorrect(), event.getGameType(), event.getGameTime(), event.getAnswerIndex(), event.getSkill());
				} else if(event.getType() == UpdateProgressEvent.TYPE_UPDATE_TOPIC) {
					onFinishGame(false);
				} else if(event.getType() == UpdateProgressEvent.TYPE_UPDATE_CHILD_CARD) {
					updateChildCardProgress(event.getCardId(), event.getChildId(), event.getCorrect());
				}
			}
		}));
		
		addHandlerRegistration(eventBus.addHandler(ReloadGameEvent.TYPE, new ReloadGameEventHandler() {
			
			@Override
			public void onReloadGame(ReloadGameEvent event) {
				maxQuestion = LoginManager.getUserSetting().getNumberQuestion();
				updateTopicsProgress();
				if(cards != null && !cards.isEmpty() && playTopics != null && !playTopics.isEmpty()){
					cards.clear();
					loadData();
				} else {
					loadMoreCardsInfors(cards, avarageProgress);
				}
			}
		}));
		
//		addHandlerRegistration(eventBus.addHandler(ShareFacebookEvent.TYPE, new ShareFacebookEventHandler() {
//			@Override
//			public void onShareFacebook(ShareFacebookEvent event) {
//				if(!ClientUtils.isOnline(true)){
//					return;
//				}
//				SocialSharing.shareFacebook(event.getGameObject());
//			}
//		}));
		addHandlerRegistration(newBasicGameView.getSettingButton().addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				boolean isShowGameWidget = !(newBasicGameView.getGameObject() instanceof ParagraphGameObject);
				newBasicGameView.getPopupSetting().showGameWidgetSetting(isShowGameWidget);
				newBasicGameView.getPopupSetting().show(newBasicGameView.getGameObject(), newBasicGameView.getGameCores());
			}
		}));
		
		addHandlerRegistration(newBasicGameView.getDictionButton().addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				onDictionaryMode();
			}
		}));
		
		addHandlerRegistration(newBasicGameView.getButtonShare().addTapHandler(new TapHandler() {
			
			@Override
			public void onTap(TapEvent event) {
				if(!ClientUtils.isOnline(true)){
					return;
				}
				shareQuestion(newBasicGameView.getGameObject());
			}
		}));
		
		addHandlerRegistration(eventBus.addHandler(DictionaryTapEvent.TYPE, new DictionaryTapEventHandler() {
			
			@Override
			public void excute(DictionaryTapEvent event) {
				if(event.getDictionaryType() == DictionaryType.OUT) {// && LoginManager.getUserSetting().getSearchDirect() == 1){
					if(AppConfig.LANGUAGE_ID == LoginManager.getUserSetting().getNativeLangId()){
						return;
					}
					if(!clientFactory.getDictionarySearchDialog().isGoogleTranslate(event.getTextSearch(), event.getWidget(), AppConfig.LANGUAGE_ID, LoginManager.getUserSetting().getNativeLangId())){
						clientFactory.getDictionarySearchDialog().getSearchBox().setText(event.getTextSearch());
						clientFactory.getDictionarySearchDialog().searchDictonary(event.getTextSearch(), event.getWidget());
					}
				}
			}
		}));
		addHandlerRegistration(eventBus.addHandler(PlaySoundDoneEvent.TYPE, new PlaySoundDoneEventHandler() {
				@Override
				public void onPlaySoundDone(PlaySoundDoneEvent event) {
					newBasicGameView.onSoundDone();
				}
		}));
		addHandlerRegistration(eventBus.addHandler(PlayRecordDoneEvent.TYPE, new PlayRecordDoneEventHandler() {
			
			@Override
			public void onPlayRecordDone(PlayRecordDoneEvent doneEvent) {
				ClientUtils.log("done listendddddd");
				newBasicGameView.playRecordDone();
			}
		}));
		
		addHandlerRegistration(eventBus.addHandler(SaveRecordAudioEvent.TYPE, new SaveRecordAudioEventHandler() {
			
			@Override
			public void onExcute(SaveRecordAudioEvent audioEvent) {
				String urlRecord = audioEvent.getUrlRecord();
				newBasicGameView.saveAudioRecord(urlRecord);
			}
		}));
		addHandlerRegistration(eventBus.addHandler(TurnonRecordeAudioEvent.TYPE, new TurnonRecordeAudioEventHandler() {
			
			@Override
			public void onExcute(TurnonRecordeAudioEvent audioEvent) {
				newBasicGameView.userNotTurnOnPermisonRecord();
			}
		}));
		
		addHandlerRegistration(newBasicGameView.getStudyNewCardDialog().getHeaderPanel().getBackButton().addTapHandler(new TapHandler() {
			
			@Override
			public void onTap(TapEvent event) {
				onBackButtonPressed();
			}
		}));
	}
	
	@Override
	protected void translate(){
		newBasicGameView.getCurrentGameCore().setDictionaryMode(!newBasicGameView.getCurrentGameCore().getDictionaryMode());
		newBasicGameView.getCurrentGameCore().showDictionaryMode();
		newBasicGameView.setIconDictionaryMode(newBasicGameView.getCurrentGameCore().getDictionaryMode());
	}
	
	private void shareQuestion(final GameObject gameObject) {
		if(newBasicGameView.getButtonShare().isClick()){
			return;
		}
		String questionIds = "";
		for (int i = 0; i < gameObject.getCardIds().size(); i++) {
			Long cardId = gameObject.getCardIds().get(i);
			questionIds += cardId;
			if(i != gameObject.getCardIds().size() - 1){
				questionIds += "-";
			}
		}
		final String data = Config.QUESTION_TOKEN + questionIds + Config.DATABASE_TOKEN + 
				gameObject.getDatabase() + Config.GAMETYPE_TOKEN + gameObject.getGameObjectType();
		ClientUtils.log("Share data : " + data);
		if(!LittleZeros.phoneGap.isPhoneGapDevice()){
			return;
		}
		ScreenShotManager.getScreenShot("sreenShot_" + new Date().getTime()+ "_", 50,
			new AsyncCallback<String>() {
				@Override
				public void onFailure(Throwable caught) {
					LittleZeros.hideLoading();
				}
				@Override
				public void onSuccess(String imageUrl) {
					newBasicGameView.getButtonShare().setClick(true);
					newBasicGameView.getButtonShare().getImage().setUrl("images/loader.gif");
					if(imageUrl != null && !imageUrl.isEmpty() 
							&& MGWT.getOsDetection().isAndroid() && !imageUrl.contains("file://"))
						imageUrl = "file://" + imageUrl;
					imageScreelShot = imageUrl;
					ClientUtils.log("Image file Screenshot===>" + imageUrl);
					FileTransfer.uploadImageFromLocal(imageUrl, new AsyncCallback<String>() {
						
					@Override
					public void onSuccess(String result) {
						FileManager.deleteFile(getDirFromPath(imageScreelShot), getNameFromPaht(imageScreelShot));
						LittleZeros.hideLoading();
						String link = "https://fb.me/" + AppConfig.FACEBOOK_SHARETOKEN + "?" + data ;
						if(exit == false){
							FacebookManager.shareFacebook(result, AppConfig.APP_NAME, "loxoapps.com", link);
						}
						newBasicGameView.getButtonShare().getImage().setUrl("images/shareApp.png");
						newBasicGameView.getButtonShare().setClick(false);
					}
						
					@Override 
						public void onFailure(Throwable caught) {
						LittleZeros.hideLoading();
							newBasicGameView.getButtonShare().setClick(false);
						}
					});
				}
			});
	}
	private String getDirFromPath(String path){
		return path.substring(0, path.lastIndexOf("/"));
	}
	private String getNameFromPaht(String path){
		return path.substring( path.lastIndexOf("/")+1,path.length());
	}
	@Override
	protected void onKeyBoardShow(boolean isShow) {
		super.onKeyBoardShow(isShow);
		ClientUtils.log("newBasicGameView onKeyBoardShow");
		newBasicGameView.onKeyboardShowHide(isShow);
	}
	
	@Override
	protected void loadData() {
		ClientUtils.log("Game activity load data: mainTopic " + (mainTopic != null ? " != null " : " null"));
		cardProgressesToUpdate.clear();
		cardUserStatisticsToUpdate.clear();
		if(cards != null && !cards.isEmpty()){
			for(final Topic topic : playTopics) {
				if(topic.getProgress() != null) {
					avarageProgress += topic.getProgress().getProgress();
				}
			}
			loadMoreCardsInfors(cards, avarageProgress);
		} else if(cardIdMaps == null || cardIdMaps.isEmpty()){
			final Map<Long, List<Long>> parentIdMap = new HashMap<Long, List<Long>>();
			int totalProgress = 0;
			for(final Topic topic : playTopics) {
				if(topic.getProgress() != null) {
					totalProgress += topic.getProgress().getProgress();
				}
				ClientUtils.log("GameActivity.loadData(): " + topic.getName() + ":" + topic.getId() + ":" 
						+ topic.getTotalCardNum() + ":" + topic.getChildrentCards().size() + ":" + topic.getChildrentTopics().size() + ":" + topic.getChildrentIds());
				final List<Long> parentIds = new ArrayList<Long>();
				if (!topic.getChildrentTopics().isEmpty()) {// Topic nomal and vocabulary
					for(Topic child : topic.getChildrentTopics()) {
						parentIds.addAll(child.getChildrentIds());
					}
				}
				else if (topic.getTotalCardNum() > 0 || !topic.getChildrentIds().isEmpty()) {
					if(topic.getParentId() == Config.MYSPACE_ID || topic.getUserId() != null && !topic.getUserId().isEmpty()){
						ClientUtils.log("get my space");
						parentIds.add(topic.getId());
					} else {
						parentIds.addAll(topic.getChildrentIds());
					}
				}
				parentIdMap.put(topic.getId(), parentIds);
			}
			avarageProgress = (int)(totalProgress/playTopics.size());
			getCards(parentIdMap);
		} else {
			final Map<Long, Card> cardToPlays = new HashMap<Long, Card>();
			getCards(cardIdMaps, new AsyncCallback<List<Card>>() {
				@Override
				public void onFailure(Throwable caught) {
					if(countLoadCard == cardIdMaps.size()){
						loadAnyMoreCardInfor(cardToPlays);
					}
				}

				@Override
				public void onSuccess(List<Card> result) {
					if(result != null){
						for (Card card : result) {
							cardToPlays.put(card.getId(), card);
						}
					}
					if(countLoadCard == cardIdMaps.size()){
						loadAnyMoreCardInfor(cardToPlays);
					}
				}
			});
		}
	}
	
	protected void loadAnyMoreCardInfor(final Map<Long, Card> cardToPlays){
		if(cardToPlays.isEmpty() || playTopics == null || playTopics.isEmpty()){
			Toaster.showToast(Locale.get().noDataFound());
			return;
		}
		ClientUtils.adminLog("loadAnyMoreCardInfor : " + cardToPlays.size());
		ClientData.cardProgressManager.getCardsProgress(new ArrayList<Card>(cardToPlays.values()), playTopics, new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				loadMoreCardsInfors(new ArrayList<Card>(cardToPlays.values()), avarageProgress);
			}

			@Override
			public void onSuccess(Void result) {
				loadMoreCardsInfors(new ArrayList<Card>(cardToPlays.values()), avarageProgress);
			}
		});
	}
	
	int countLoadCard = 0;
	private void getCards(final Map<Integer, List<Long>> cardIdMaps, final AsyncCallback<List<Card>> callback){
		countLoadCard = 0;
		for (final Integer databaseType : cardIdMaps.keySet()) {
			final List<Long> cardIds = cardIdMaps.get(databaseType);
			if(cardIds != null && !cardIds.isEmpty()){
				if(databaseType == 0){
					countLoadCard ++;
					callback.onFailure(new Throwable());
				} else
					ClientData.getCardManager(databaseType).getCards(cardIds, true, new AsyncCallback<List<Card>>() {
	
						@Override
						public void onFailure(Throwable caught) {
							countLoadCard ++;
							callback.onFailure(caught);
						}
	
						@Override
						public void onSuccess(final List<Card> cards) {
							if(cards != null && !cards.isEmpty()){
								loadCardProgress(cardIds, cards, databaseType, new AsyncCallback<Void>() {
	
									@Override
									public void onFailure(Throwable caught) {
										countLoadCard ++;
										ClientUtils.adminLog("cards size : " + cards.size());
										callback.onSuccess(cards);
									}
	
									@Override
									public void onSuccess(Void result) {
										countLoadCard ++;
										ClientUtils.adminLog("cards size : " + cards.size());
										callback.onSuccess(cards);
									}
								});
							} else {
								ClientUtils.log("databaseType : " + databaseType + " -- " + cardIds.toString() + " nulll") ;
								countLoadCard ++;
								callback.onSuccess(null);
							}
						}
					});
			} else {
				countLoadCard ++;
				callback.onFailure(new Throwable());
			}
		}
	}
	
	protected void loadCardProgress(final List<Long> cardIds, final List<Card> cards, final int database, final AsyncCallback<Void> loadedData){
		ClientData.cardProgressManager.getCardsProgressByIds(cardIds, new AsyncCallback<List<CardProgress>>() {

			@Override
			public void onFailure(Throwable caught) {
				loadedData.onSuccess(null);
			}

			@Override
			public void onSuccess(List<CardProgress> result) {
				if(result != null){
					Map<Long, CardProgress> cardProgressMap = new HashMap<Long, CardProgress>();
					Map<Long, Long> topicIds = new HashMap<Long, Long>();
					for (CardProgress progress : result) {
						cardProgressMap.put(progress.getCardId(), progress);
						topicIds.put(progress.getParentId(), progress.getParentId());
					}
					for (Card card : cards) {
						card.setDatabase(database);
						CardProgress cardProgress = cardProgressMap.get(card.getId());
						card.setProgress(cardProgress);
						if(cardProgress != null){
							card.setTopicId(cardProgress.getParentId());
						}
					}
					loadTopics(new ArrayList<Long>(topicIds.values()), cards, database, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							loadedData.onSuccess(null);
						}

						@Override
						public void onSuccess(Void result) {
							loadedData.onSuccess(null);
						}
					});
				} else {
					loadedData.onSuccess(null);
				}
			}
		});
	}
	
	protected void loadTopics(List<Long> topicIds, final List<Card> cards, int databaseType, final AsyncCallback<Void> callback){
		ClientUtils.adminLog("xxxxxx : " + topicIds.toString());
		ClientData.getTopicsDataManager(databaseType).getTopics(topicIds, new AsyncCallback<List<Topic>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(List<Topic> result) {
				if(playTopics == null){
					playTopics = new ArrayList<Topic>();
				}
				if(result != null){
					for (Topic topic : result) {
						ClientUtils.log("yyyyy : " + topic.getName());
						if(!playTopicsMap.containsKey(topic.getId())){
							playTopics.add(topic);
							playTopicsMap.put(topic.getId(), topic);
						}
					}
					Map<Long, List<Card>> cardMaps = new HashMap<Long, List<Card>>();
					for(Card card : cards) {
						Topic topic = playTopicsMap.get(card.getTopicId());
						if(topic != null) {
							card.setTopicName(topic.getName());
						}
						List<Card> cards = cardMaps.get(card.getTopicId());
						if(cards == null){
							cards = new ArrayList<Card>();
						}
						cards.add(card);
						cardMaps.put(card.getTopicId(), cards);
					}
					for (Long topicId : cardMaps.keySet()) {
						List<Card> cards = cardMaps.get(topicId);
						Map<Long, CardProgress> maps = new HashMap<Long, CardProgress>();
						if(cards != null){
							for (Card card : cards) {
								if(card.getProgress() != null){
									maps.put(card.getId(), card.getProgress());
								}
							}
							ClientData.cardProgressManager.putCardProgress(topicId, new ArrayList<CardProgress>(maps.values()));
						}
					}
				}
				callback.onSuccess(null);
			}
		});
	}
	
	private void getCards(final Map<Long, List<Long>> parentIdMap) {
		new RPCCall<HashMap<Long, List<Card>>>() {

			@Override
			public void onFailure(Throwable caught) {
				showErrorOccuredDialog();
			}
			@Override
			public void onSuccess(HashMap<Long, List<Card>> result) {
				final List<Card> cards = new ArrayList<Card>();
				if(result != null && !result.isEmpty()) {
					for(Long topicId : result.keySet()) {
						Topic topic = playTopicsMap.get(topicId);
						for(Card card : result.get(topicId)) {
							card.setTopicId(topicId);
							if(topic != null) {
								card.setTopicName(topic.getName());
							}
						}
						cards.addAll(result.get(topicId));
					}
					ClientData.cardProgressManager.getCardsProgress(cards, playTopics, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							loadMoreCardsInfors(cards, avarageProgress);
						}

						@Override
						public void onSuccess(Void result) {
							loadMoreCardsInfors(cards, avarageProgress);
						}
					});
					
				} else showErrorOccuredDialog();
			}

			@Override
			protected void callService(AsyncCallback<HashMap<Long, List<Card>>> cb) {
				int order = studyOrder > 0 ? studyOrder : LoginManager.getUserSetting().getStudyOrder();
				ClientData.getCardManager(mainTopic.getDatabase()).getCards(parentIdMap, maxQuestion * 3, order, cb);
			}
		}.retry(1, false, false);
	}
	
	protected void showErrorOccuredDialog() {
		Topic topic = null;
		if(playTopics.size() == 1)
			topic = playTopics.get(0);
		else topic = mainTopic;
		goTo(new AboutTopicPlace(new CategoryPlace(mainTopic, null), topic, mainTopic, Tab.ALL));
	}
	
	protected void loadMoreCardsInfors(final List<Card> cards, final int progress) {
		ClientData.cardStatistic.getCardStatisticsByCards(cards, null);
		createGameObjects(cards, progress);
	}
	
	protected List<Card> prepareCardToPlay(List<Card> cards){
		if(ClientData.SCOREDATA_MANAGER.getScores().isEmpty()){
			ClientUtils.shuffle(cards);
			Card paragraph = null;
			List<Card> results = new ArrayList<Card>();
			for (Card card : cards) {
				if(card.getHasChild() == Config.TYPE_CARD_PARAGRAP && paragraph == null 
					|| card.getType() == Config.TYPE_TOPIC_SPEAKING || card.getType() == Config.TYPE_TOPIC_WRITING){
					paragraph = card;
				}
				if(card.getHasChild() == Config.TYPE_CARD_NOMAL){
					results.add(card);
				}
			}
			if(paragraph != null){
				results.add(paragraph);
			}
			return results;
		} else {
			return cards;
		}
	}
	
	protected void createGameObjects(List<Card> cards, int progress) {
		if(cards == null || cards.isEmpty()) {
			Toaster.showToast(Locale.get().noData());
			try {
				onFinishGame(true);
			} catch (Exception e) {
				Place previousPlace = ((BasicPlace)place).getPreviousPlace();
				goTo(previousPlace);
			}
			return;
		}
		List<Card> cardAfterPrepares = prepareCardToPlay(cards);
		error = null;
		ClientUtils.adminLog("Currrent progress is: " + progress + "--" + cardAfterPrepares.size() + " _ " );
		this.cards = new ArrayList<Card>(cardAfterPrepares);
		Map<Long, Card> cardsMap = new HashMap<Long, Card>();
		for(Card card : cardAfterPrepares) {
			ClientUtils.log("beforeSortedCards : " + card.getId() + " -- " + (card.getProgress() != null ? card.getProgress().getBoxNum() : ""));
			card.setDefaultLanguage(AppConfig.LANGUAGE_ID);
			card = cardsMap.put(card.getId(), card);
		}
		List<Card> sortedCards = null;
		if(studyOrder == Config.STUDY_ORDER_WRONG_FIRST){
			sortedCards = sortCards(GameUtils.getSortedCardsToPlay(cardAfterPrepares,-1, studyOrder));
		} else {
			if(LoginManager.getUserSetting().getStudyOrder() == Config.STUDY_ORDER_NORMAL){
//				sortedCards = GameUtils.getSortedCardsToPlay(cardAfterPrepares,-1, LoginManager.getUserSetting().getStudyOrder());
//				List<Card> neverStudies = new ArrayList<Card>();
//				for (Card card : sortedCards) {
//					if(!neverStudies.contains(card) && (card.getProgress() == null || card.getProgress().getHistory() == null 
//										|| card.getProgress().getHistory().isEmpty())){
//						neverStudies.add(card);
//					}
//				}
//				if (!neverStudies.isEmpty()) {
//					sortedCards.removeAll(neverStudies);
//					sortedCards.addAll(0, neverStudies);
//				}
				sortedCards = cardAfterPrepares;
				Collections.sort(sortedCards, new Comparator<Card>() {

					@Override
					public int compare(Card o1, Card o2) {
						return o1.getBox() - o2.getBox();
					}
				});
			} else {
				sortedCards = sortCards(GameUtils.getSortedCardsToPlay(cardAfterPrepares,-1, LoginManager.getUserSetting().getStudyOrder()));
			}
		}
		playCardsMap.clear();
		for(Card card : sortedCards) {
			playCardsMap.put(card.getId(), card);
			card.setDefaultLanguage(AppConfig.LANGUAGE_ID);
			ClientUtils.log("aftersortedCards : " + card.getId() + " -- " + (card.getProgress() != null ? card.getProgress().getBoxNum() : ""));
		}
		currentGameObjects = new ArrayList<GameObject>();
		ClientUtils.adminLog("UserSettingGameTypes: " + LoginManager.getUserSetting().getGameTypes());
		List<Integer> gameTypes = new ArrayList<Integer>(LoginManager.getUserSetting().getGameTypes()) ;
		if (gameTypes == null || gameTypes.isEmpty())
			gameTypes = new ArrayList<Integer>(Config.getGameTypes());
		if(sortedCards.size() <= 4) {
			gameTypes.remove(new Integer(Config.GAME_MATCHING));
			gameTypes.remove(new Integer(Config.GAME_MEMORIZE));
			if (gameTypes.isEmpty()) {
				gameTypes = new ArrayList<Integer>(Config.getGameTypes());
				gameTypes.remove(new Integer(Config.GAME_MATCHING));
				gameTypes.remove(new Integer(Config.GAME_MEMORIZE));
			}
		}
		ClientUtils.adminLog("UserSettingGameTypes: " + gameTypes);
		try {
			while(currentGameObjects.size() < maxQuestion && !sortedCards.isEmpty() && !checkExitGame()) {
				ClientUtils.adminLog("While Lopp: " + sortedCards.size() + ":" + cardAfterPrepares.size());
				if(gameMode == Config.MODE_PRATICE || gameMode == Config.MODE_CHALLENGE) {
					int r = Random.nextInt(gameTypes.size());
					int gameType = gameTypes.get(r);
					Card car = sortedCards.get(0);
					ClientUtils.log("CurrentcardInfor : " + car.getId() + " -- " + (car.getProgress() != null ? car.getProgress().getBoxNum() : ""));
					int userLevel = car.getUserLevel();
					ClientUtils.log("getUserLevelxxx1 : " + car.getFrontText() + " -- " +userLevel);
					boolean isVocabylary = car.getDatabase() == Config.VOCABULARY_DATABASE;
					gameType = ClientUtils.getGameType(userLevel, isVocabylary, gameTypes);
					if(isVocabylary && gameType == Config.GAME_REARRANGE){
						gameType = Config.GAME_QUIZ;
					}
					ClientUtils.adminLog("gameType: " + gameType + ":" + Config.getGameName(gameType));
					this.createGameObjectByType(cardsMap, sortedCards, gameType, progress, userLevel);
				}
				else {
					Card card = sortedCards.remove(0);
					if(cardIdMaps == null){
						currentGameObjects.add(new StudyObject(card, mainTopic.getId()));
					} else {
						currentGameObjects.add(new StudyObject(card, card.getTopicId()));
					}
				}
				if(error != null) {
					break;
				}
			}
			if(error != null) {
				Toaster.showToast(Locale.get().errorOccurs());
				return;
			}
			Timer startGameTimer = new Timer() {
				@Override
				public void run() {
					if(currentGameObjects == null || currentGameObjects.isEmpty() || checkExitGame()){
						cancel();
						return;
					}
					if(currentGameObjects.get(0).isLoadedData() && !checkExitGame()){
						cancel();
						String topicName = "";
						if(mainTopic != null){
							mainTopic.getName();
						}
						if(playTopics != null && playTopics.size() == 1){
							topicName = playTopics.get(0).getName();
						}
						boolean forceHideStudy = !ClientData.SCOREDATA_MANAGER.getScores().isEmpty() && mainTopic != null && mainTopic.getType() == Config.TYPE_DATA_QUIZ;
						newBasicGameView.prepareGame(currentGameObjects, topicName,
								mainTopic == null || mainTopic.getType() != Config.TYPE_DATA_QUIZ
								|| Config.isListeningSkillSupported(), forceHideStudy);
					}
				}
			};
			startGameTimer.scheduleRepeating(500);
		} catch (Exception e) {
			ClientUtils.log("createGameObjects error : " + e.getMessage());
		}
	}
	
	
	private boolean checkExitGame(){
		return !(clientFactory.getBasicView() != null && clientFactory.getBasicView() instanceof GameView);
	}
	
	private void createGameObjectByType(Map<Long, Card> cardsMap, List<Card> sortedCards, int gameType, int progress, int userLevel) {
		Card nextCard = sortedCards.get(0);
		if (gameType == Config.GAME_REARRANGE && !ClientUtils.supportRearrange(nextCard)
			||( cardIdMaps != null && !cardIdMaps.isEmpty() && (gameType == Config.GAME_MATCHING || gameType == Config.GAME_MEMORIZE))) {
			gameType = Config.GAME_QUIZ;
		}
		if(nextCard.getType() == Config.TYPE_TOPIC_SPEAKING){
			gameType = Config.GAME_SPEAKING;
			nextCard = sortedCards.remove(0);
			GameObject gameObject = new SpeakingGameObject(nextCard, gameType, progress, gameMode, userLevel);
			currentGameObjects.add(gameObject);
			return;
		}
		if(nextCard.getType() == Config.TYPE_TOPIC_WRITING){
			gameType = Config.GAME_WRITING;
			nextCard = sortedCards.remove(0);
			GameObject gameObject = new WritingGameObject(nextCard, gameType, progress, gameMode, userLevel);
			currentGameObjects.add(gameObject);
			return;
		}
		if (!Config.isListeningSkillSupported() && 
				(!nextCard.getBackHint().isEmpty() || !nextCard.getFrontHint().isEmpty())) {
			String hint = !nextCard.getFrontHint().isEmpty() ? nextCard.getFrontHint() : nextCard.getBackHint();
			if (!hint.trim().equalsIgnoreCase(nextCard.getBackText().trim())) {
				gameType = Config.GAME_QUIZ;
			}
		}
		if(nextCard.getBackTexts() != null && !nextCard.getBackTexts().isEmpty()){
			gameType = Config.GAME_QUIZ;
			nextCard = sortedCards.remove(0);
			GameObject gameObject = new QuizGameObject(nextCard, nextCard.getTopicId(), userLevel);
			currentGameObjects.add(gameObject);
			return;
		} else if(nextCard.getHasChild() == 1) {
			gameType = Config.GAME_PARAGRAPH;
			Card card = sortedCards.remove(0);
			ClientUtils.log("create paragraph game object");
			currentGameObjects.add(new ParagraphGameObject(card, true, mainTopic != null ? mainTopic.getId() : AppConfig.MAIN_TOPIC_ID));
			return;
		}
		else if (nextCard.getType() != Config.TYPE_DATA_FLASHCARD && nextCard.hasMultichoices()
				//&& gameType != Config.GAME_REARRANGE 
				&& gameType != Config.GAME_SPELLING) {
			ClientUtils.log("MultiChoices: " + nextCard.getFrontText() + ":" + nextCard.getMultiChoices());
			gameType = Config.GAME_QUIZ;
		}
		if (gameType == Config.GAME_REARRANGE || gameType == Config.GAME_SPELLING
				|| gameType == Config.GAME_SPELLING_TYPING) {
			if (nextCard.getBackText() != null && !nextCard.getBackText().isEmpty()
					&& nextCard.getBackText().contains("/"))
				gameType = Config.GAME_QUIZ;
		}
		if((gameType == Config.GAME_SPELLING || gameType == Config.GAME_SPELLING_TYPING) 
//			&& !ClientUtils.isNativeLetter(nextCard.getBackText().trim())) {
			&& !ClientUtils.isNativeLetter(nextCard.getFrontText().trim())) {
			gameType = Config.GAME_QUIZ;
		}
		if (gameType == Config.GAME_QUIZ) {
			Card card = sortedCards.remove(0);
			ClientUtils.log("create quiz game object");
			GameObject gameObject = null;//new QuizGameObject(card, mainTopic != null ? mainTopic.getId() : AppConfig.MAIN_TOPIC_ID);
			boolean hasMultiChoices = (card.getMultiChoices() != null && !card.getMultiChoices().isEmpty()) || (card.getBackTexts() != null && !card.getBackTexts().isEmpty());
			if (mainTopic != null && !hasMultiChoices && mainTopic.getType() == Config.TYPE_DATA_QUIZ) {
				if(ClientUtils.supportSpelling(card)){
					gameObject = createSpellingGameObject(card, mainTopic != null ? mainTopic.getId() : AppConfig.MAIN_TOPIC_ID, userLevel);
				} else {
					gameObject = new QuizGameObject(card, gameType, progress, gameMode, userLevel);
				}
			}
			else {
				if (card.getMultiChoices() != null && card.getMultiChoices().size() > 0) {
					gameObject = new QuizGameObject(card, gameType, true, userLevel);
				}
				else {
					gameObject = new QuizGameObject(card, gameType, progress, gameMode, userLevel);
				}
			}
			currentGameObjects.add(gameObject);
		}
		else if (gameType == Config.GAME_MATCHING || gameType == Config.GAME_MEMORIZE) {
			try{
				createMatchingGameObject(gameType, sortedCards, new ArrayList<Card>(cards), cardsMap, progress, userLevel);
			} catch (Exception e) {
				Toaster.showToast("Error of matching game object: " + e.getMessage());
				error = e.getMessage();
			}
		}
		else if (gameType == Config.GAME_SPELLING) {
			try {
				if (!ClientUtils.supportSpelling(nextCard) ||
						AppConfig.MAIN_TOPIC_ID.longValue() == Config.APP_TOEIC_ID.longValue() && nextCard.hasMultichoices()) {
					this.createGameObjectByType(cardsMap, sortedCards, Config.GAME_QUIZ, progress, userLevel);
					return;
				}
				if (Config.LANGUAGE_NOT_SUPPORT_SPELLING.contains(AppConfig.LANGUAGE_ID)
						|| ClientUtils.supportOnlyTypingSpelling(nextCard)) {
						gameType = Config.GAME_SPELLING_TYPING;
				}
				currentGameObjects.add(new SpellingGameObject(sortedCards.remove(0), gameType, progress, Config.NULL_ID, userLevel));
			} catch (Exception e) {
				Toaster.showToast("Error of spelling game object: " + e.getMessage());
				error = e.getMessage();
			}
		}
		else if (gameType == Config.GAME_REARRANGE) {
			RearrangeGameObject gameObject = new RearrangeGameObject(sortedCards.remove(0), sortedCards, gameType, progress, gameType, 0L, userLevel);
			currentGameObjects.add(gameObject);
			return;
		}
		else if (gameType == Config.GAME_TRUE_FALSE) {
			nextCard = sortedCards.remove(0);
			GameObject gameObject = new TrueFalseGameObject(nextCard, gameType, progress, gameMode, userLevel);
			currentGameObjects.add(gameObject);
			return;
		} else {
			gameType = Config.GAME_PARAGRAPH;
			Card card = sortedCards.remove(0);
			ClientUtils.log("create paragraph game object");
			currentGameObjects.add(new ParagraphGameObject(card, true, mainTopic != null ? mainTopic.getId() : AppConfig.MAIN_TOPIC_ID));
		}
	}
	
	private static GameObject createSpellingGameObject(Card card, Long topicId, int userLevel) {
		SpellingGameObject spellingGameObject = new SpellingGameObject(0, topicId, card.getParentId(), card.getDatabase());
		spellingGameObject.setId(card.getId());
		spellingGameObject.setParentId(card.getTopicId());
		Face questionFace = new Face(Config.FACE_FRONT, Config.GAME_SPELLING_TYPING, card.getId(), 
				ClientUtils.getImageContent(card.getFrontText(),true), card.getFrontImage(), "", card.getFrontLanguage(), card.getTypeOfWord());
		Face answerFace= new Face(Config.FACE_BACK, Config.GAME_SPELLING_TYPING, card.getId(), 
				ClientUtils.getImageContent(card.getBackText(),true), card.getBackImage(), "", card.getBackLanguage(),card.getTypeOfWord());
		answerFace.setHint(card.getFrontHint());
		questionFace.setHint(card.getFrontHint());
		spellingGameObject.setQuestion(questionFace);
		spellingGameObject.setAnswer(answerFace);
		spellingGameObject.setGameObjectType(Config.GAME_SPELLING_TYPING);
		spellingGameObject.setTopicName(card.getTopicName());
		spellingGameObject.setTopicId(topicId);
		return spellingGameObject;
	}
	
	protected void createMatchingGameObject(int gameType, List<Card> sortedCards, List<Card> allCards, 
					Map<Long, Card> allCardsMap, int progress, int userLevel) {
		try {
			int questionNum = gameMode == Config.MODE_TEST ? 4 : progress > 70 ? 4 : progress > 30 ? 3 : 2;
			if(sortedCards.size() >= 2) {
				Card mCard = getMatchingCards(sortedCards,allCards, questionNum, 0);
				ClientUtils.adminLog("createMatchingGameObject : " + sortedCards.size());
				if (mCard == null)
					return;
				MatchingGameObject gameObject = new MatchingGameObject(mCard, sortedCards,allCards, 
						gameType, questionNum, Config.MODE_PRATICE, mainTopic.getId(),playCardsMap, userLevel);
				if (gameObject.getMatchingObjects().size() >= 4) {
					currentGameObjects.add(gameObject);
					addCardToCardsMap(gameObject, allCardsMap);
				}
			}
			else {
				if(sortedCards.isEmpty())
					return;
				else sortedCards.remove(0);
			}
		}
		catch (Exception e) {
		}
	}
	
	private void addCardToCardsMap(MatchingGameObject gameObject, Map<Long, Card> allCardsMap) {
		for(Face face : gameObject.getMatchingObjects()) {
			Long id = face.getCardId();
			if(!playCardsMap.containsKey(id)) {
				playCardsMap.put(id, allCardsMap.get(id));
			}
		}
	}
	
	public static Card getMatchingCards(List<Card> sortedCards, List<Card> allCards, int maxQuestion, int roundNum) {
		int index = -1;
		ClientUtils.log("GameActivity.getMatchingCards: " + sortedCards.size() + ":" + allCards.size() + ":" + maxQuestion);
		if (sortedCards.isEmpty() || allCards.isEmpty() || roundNum >= 10)
			return null;
		for(int i= 0;i<sortedCards.size();i++) {
			Card card = sortedCards.get(i);
			if(AppConfig.APP_TYPE == AppConfig.LANGUAGE || !card.isLongCard()) {
				index = i;
				break;
			}
		}
		if(index > -1)
			return sortedCards.remove(index);
		else if(!allCards.isEmpty()){
			int order = studyOrder > 0 ? studyOrder : LoginManager.getUserSetting().getStudyOrder();
			sortedCards = sortCards(GameUtils.getSortedCardsToPlay(allCards,sortedCards.size()*maxQuestion, 
					order));
			return getMatchingCards(sortedCards, allCards, maxQuestion, roundNum + 1);
		} else {
			return null;
		}
	}
	
	protected static List<Card> sortCards(List<Card> cards) {
		Map<Integer, List<Card>> boxCardMap = new HashMap<Integer, List<Card>>();
		Map<Long, Boolean> topicCardsMap = new HashMap<Long, Boolean>();
		Map<String, List<Card>> cardsMap = new HashMap<String, List<Card>>();
		for(Card card : cards) {
			int box = card.getProgress() != null ? card.getProgress().getBoxNum() : 1;
			if(!boxCardMap.containsKey(box))
				boxCardMap.put(box, new ArrayList<Card>());
			boxCardMap.get(box).add(card);
			if(!topicCardsMap.containsKey(card.getTopicId())) {
				topicCardsMap.put(card.getTopicId(), true);
			}
			String key = box + "_" + card.getTopicId();
			if(!cardsMap.containsKey(key))
				cardsMap.put(key, new ArrayList<Card>());
			cardsMap.get(key).add(card);
		}
		List<Integer> boxList = new ArrayList<Integer>(boxCardMap.keySet());
		List<Long> topicIds = new ArrayList<Long>(topicCardsMap.keySet());
		Collections.sort(boxList);
		List<Card> returnCards = new ArrayList<Card>();
		for(Integer box : boxList) {
			List<Card> rCards = new ArrayList<Card>();
			List<Card> sCards = boxCardMap.get(box);
			int currentTopicIndex = 0;
			while (rCards.size() < sCards.size()) {
				if(currentTopicIndex < topicIds.size()) {
					Long topicId = topicIds.get(currentTopicIndex);
					String key = box + "_" + topicId;
					if(cardsMap.containsKey(key)) {
						List<Card> kCards = cardsMap.get(key);
						if(!kCards.isEmpty()) {
							rCards.add(kCards.remove(0));
						}
						else {
							cardsMap.remove(key);
						}
					}
					currentTopicIndex++;
					if(currentTopicIndex == topicIds.size()) {
						currentTopicIndex = 0;
					}
				}	
			}
			returnCards.addAll(rCards);
		}
		return returnCards;
	}
	
	protected void updateCardProgress(Long cardId, int correct, int gameType, int gameTime, int answerIndex, int skill) {
		if(!LoginManager.isLoggedIn(false))
			return;
		ClientUtils.log("updateCardProgress : " + cardId);
		if(playCardsMap.containsKey(cardId)) {
			Card card = playCardsMap.get(cardId);
			CardProgress cardProgress = card.getProgress();
			String id = AppConfig.MAIN_TOPIC_ID + "_" + cardId + "_" + LoginManager.youId();
			int cardType = card.getHasChild() == 1 ? Config.TYPE_CARD_PARAGRAP : Config.TYPE_CARD_NOMAL;
			int dataType = card.getDatabase();
			if (cardProgress == null) {
				cardProgress = new CardProgress(id,cardId, card.getTopicId());
				cardProgress.setUserId(LoginManager.getCurrentUser().getId());
				cardProgress.setRealParentId(card.getParentId());
				cardProgress.onAnswer(correct,gameType, true);
				// v2 : add new infor to card progress and statistic
				cardProgress.setLastResult(correct);
				cardProgress.setDataType(dataType);
				cardProgress.setCardType(cardType);
				cardProgress.setAppId(AppConfig.MAIN_TOPIC_ID);
				cardProgress.updateReviewDate(ClientUtils.getTodayTime());
				cardProgressesToUpdate.put(cardProgress.getId(), cardProgress);
			} else {
				//Correct - incorect had updated when user playing
				CardProgress newCardProgress = new CardProgress(cardProgress);
				newCardProgress.setHistory(cardProgress.getHistory());
				newCardProgress.setGamesPlayed(cardProgress.getGamesPlayed());
				newCardProgress.onAnswer(correct,gameType, gameType == Config.GAME_MATCHING || gameType == Config.GAME_MEMORIZE);
				// v2 : add new infor to card progress and statistic
				newCardProgress.setLastResult(correct);
				newCardProgress.setDataType(dataType);
				newCardProgress.setCardType(cardType);
				newCardProgress.setRealParentId(card.getParentId());
				newCardProgress.setAppId(AppConfig.MAIN_TOPIC_ID);
				// v3 : set review date for card progress
				newCardProgress.updateReviewDate(ClientUtils.getTodayTime());
				cardProgressesToUpdate.put(newCardProgress.getId(), newCardProgress);
			}
			//for CardUserStatistic TOPIC
			CardUserStatistic cardUserStatisticTopic = cardUserStatisticsToUpdate.get(card.getTopicId());
			if(cardUserStatisticTopic == null){
				cardUserStatisticTopic = new CardUserStatistic(LoginManager.youId(), card.getTopicId(), AppConfig.MAIN_TOPIC_ID);
			}
			updateInforCardUserStatistic(cardUserStatisticTopic, card, correct);
			//For CardUserStatistic SKILL
			Long topicId = Long.valueOf(skill);
			CardUserStatistic cardUserStatisticSkill = cardUserStatisticsToUpdate.get(topicId);
			if(cardUserStatisticSkill == null){
				cardUserStatisticSkill = new CardUserStatistic(LoginManager.youId(), topicId, AppConfig.MAIN_TOPIC_ID);
			} 
			updateInforCardUserStatistic(cardUserStatisticSkill, card, correct);
			cardUserStatisticsToUpdate.put(cardUserStatisticTopic.getTopicId(), cardUserStatisticTopic);
			cardUserStatisticsToUpdate.put(cardUserStatisticSkill.getTopicId(), cardUserStatisticSkill);
		} else {
			ClientUtils.log("playCardsMap. not containsKey(cardId)");
		}
	}
	
	private CardUserStatistic updateInforCardUserStatistic(CardUserStatistic cardUserStatisticTopic, Card card, int correct){
		cardUserStatisticTopic.addAnswerNum(1);
		if(correct == Config.ANSWER_CORRECT){
			cardUserStatisticTopic.addCorrectNum(1);
			cardUserStatisticTopic.addCorrectIds(Arrays.asList(card.getId()), card.getDatabase());
		} else {
			cardUserStatisticTopic.addIncorrectNum(1);
			cardUserStatisticTopic.addIncorrectIds(Arrays.asList(card.getId()), card.getDatabase());
		}
		return cardUserStatisticTopic;
	} 
	
	protected void updateCardProgress(List<Long> cardIds, int correct, int gameType, int gameTime, int answerIndex, int skill) {
		for(Long id : cardIds) {
			updateCardProgress(id, correct, gameType, gameTime, answerIndex, skill);
		}
	}
	
	/**
	 * Update progress for child car of paragraph
	 * */
	protected void updateChildCardProgress(Long cardId, Long childId, int correct){
		if(!LoginManager.isLoggedIn(false))
			return;
		if(playCardsMap.containsKey(cardId)) {
			Card card = playCardsMap.get(cardId);
			if(card == null || card.getChildCards() == null){
				return;
			}
			for (Card child : card.getChildCards()) {
				if(child.getId().longValue() == childId.longValue()){
					CardProgress cardProgress = child.getProgress();
					String id = AppConfig.MAIN_TOPIC_ID + "_" + childId + "_" + LoginManager.youId();
					int dataType = card.getDatabase();
					if (cardProgress == null) {
						cardProgress = new CardProgress(id, childId, card.getTopicId());
						cardProgress.setUserId(LoginManager.getCurrentUser().getId());
						cardProgress.setLastResult(correct);
						cardProgress.setDataType(dataType);
						cardProgress.setCardType(Config.TYPE_CARD_PARAGRAP_CHILD);
						cardProgress.setAppId(AppConfig.MAIN_TOPIC_ID);
						cardProgress.onAnswer(correct, Config.GAME_PARAGRAPH, true);
						cardProgress.setReviewDateOnly(Config.NULL_ID);
						cardProgressesToUpdate.put(cardProgress.getId(), cardProgress);
					} else {
						CardProgress newCardProgress = new CardProgress(cardProgress);
						newCardProgress.setHistory(cardProgress.getHistory());
						newCardProgress.setLastResult(correct);
						newCardProgress.setDataType(dataType);
						newCardProgress.setCardType(Config.TYPE_CARD_PARAGRAP_CHILD);
						newCardProgress.setAppId(AppConfig.MAIN_TOPIC_ID);
						cardProgress.onAnswer(correct, Config.GAME_PARAGRAPH, true);
						newCardProgress.setParentId(card.getTopicId());
						newCardProgress.setReviewDateOnly(Config.NULL_ID);
						cardProgressesToUpdate.put(newCardProgress.getId(), newCardProgress);
					}
					break;
				}
			}
		}
	}
	
	//TODO
	/**
	 * Update card progress and topic progress when end game 
	 * */
	protected void updateTopicsProgress() {
		if(!cardProgressesToUpdate.isEmpty()) {
			for(CardProgress cardProgress : cardProgressesToUpdate.values()) {
				Card card = playCardsMap.get(cardProgress.getCardId());
				int progress = 0;
				if(card != null) {
					Topic topic = playTopicsMap.get(card.getTopicId());
					if(topic != null) {
						topic.getCardProgressMap().put(cardProgress.getCardId(), cardProgress);
					}
					if(cardProgress.getRealParentId() == null || cardProgress.getRealParentId() <= 0){
						cardProgress.setRealParentId(card.getParentId());
					}
					if(card.getChildCards() != null && !card.getChildCards().isEmpty()){
						for (Card child : card.getChildCards()) {
							String id = AppConfig.MAIN_TOPIC_ID + "_" + child.getId() + "_" + LoginManager.youId();
							CardProgress childProgress = cardProgressesToUpdate.get(id);
							child.setProgress(childProgress);
							if(childProgress != null && childProgress.getLastResult() == Config.ANSWER_CORRECT){
								progress ++;
							}
						}
						cardProgress.setProgress(progress * 100 / card.getChildCards().size());
					}
					card.setProgress(cardProgress);
				}
			}
			List<CardProgress> cardProgresses = new ArrayList<CardProgress>(cardProgressesToUpdate.values());
			ClientData.cardProgressManager.updateCardProgesss(cardProgresses);
			ClientData.cardDaily.updateCardDaily(cardProgresses);
		}
		List<TopicProgress> topicProgresses = new ArrayList<TopicProgress>();
		if(playTopics != null) {
			List<SystemLog> systemLogs = new ArrayList<SystemLog>();
			for(Topic topic : playTopics) {
				TopicProgress progress = updateTopicProgress(topic);
				if(progress != null)
					topicProgresses.add(progress);
				if(LoginManager.getCurrentUser().isLoggedIn() && ClientData.topicPracted.get(topic.getId()) == null){
					String messageLog = Locale.get().practiced() + " \"" + topic.getName() + "\"";
					SystemLog systemLog = new SystemLog(LoginManager.youId(), Config.NULL_ID, Config.SYSTEM_LOG_ACTIVITY, messageLog);
					systemLog.setAppId(AppConfig.MAIN_TOPIC_ID);
					systemLogs.add(systemLog);
					ClientData.topicPracted.put(topic.getId(), topic.getName());
				}
			}
			if(!systemLogs.isEmpty()){
				new DataManager().updateToServer(systemLogs);
			}
		}
		ClientData.topicProgressManager.updateTopicProgress(topicProgresses);
		updateCardStatistic();
	}
	
	//Chalenge not update
	protected void updateCardStatistic(){
		if(!cardUserStatisticsToUpdate.isEmpty()){
			ClientUtils.log("updateCardStatistic : " + cardUserStatisticsToUpdate.size());
			ClientData.cardUserStatisticManager.update(new ArrayList<CardUserStatistic>(cardUserStatisticsToUpdate.values()));
		}
	}
	
	protected TopicProgress updateTopicProgress(Topic topic) {
		if(!LoginManager.isLoggedIn(false)) {
			return null;
		}
		TopicProgress progress = topic.getProgress();
		int childCardSize = topic.calcTotalCardNum();
		if(childCardSize == 0)
			return null;
		if (progress == null) {
			String id = topic.getParentId() + "_" + topic.getId() + "_" + LoginManager.youId();
			progress = new TopicProgress(id, topic.getId());
			topic.setProgress(progress);
		} else if(progress.getPassed() == 1) {
			return null;
		}
		int topicProgress = 0;
		int minToPass = 100;
		boolean isParagrap = true;
		Map<Long, CardProgress> cardProgress = topic.getCardProgressMap();
		if(cardProgress != null && !cardProgress.isEmpty()){
			for (CardProgress cp : cardProgress.values()) {
				if(cp.getCardType() >= 0 && cp.getCardType() != Config.TYPE_CARD_PARAGRAP){
					isParagrap = false;
					break;
				}
			}
			if(isParagrap){
				int t = 0;
				minToPass = Config.PERCENT_PARAGRAP_PASS;
				for (CardProgress cp : cardProgress.values()) {
					t += cp.getProgress();
				}
				topicProgress = t/topic.getTotalCardNum();
			}
		}
		if(!isParagrap){
			ClientUtils.log("updateTopicProgress: " + progress.getId());
			int boxes0 = 0;
			int boxes1 = 0;
			int boxes2 = 0;
			int boxes3 = 0;
			int boxes4 = 0;
			int boxesM = 0;
			double t = 0;
			if(topic.getChildrentIds() != null && !topic.getChildrentIds().isEmpty()){
				ClientUtils.log("DatabaseName: " + topic.getDatabase());
				for (Long child : topic.getChildrentIds()) {
					MyCardData myCardData = ClientData.myCardIDDataManager.getMyCardData(child, topic.getDatabase());
					if(myCardData != null){
						ClientUtils.log("myCardData : " + myCardData.getId());
						boxes0 += myCardData.getCardBox1().size();
						boxes1 += myCardData.getCardBox2().size();
						boxes2 += myCardData.getCardBox3().size();
						boxes3 += myCardData.getCardBox4().size();// 3 lan
						boxes4 += myCardData.getCardBoxn().size();// 4lan
						boxesM += myCardData.getCardBoxm().size();
					} else {
						ClientUtils.log("myCardData is null");
					}
				}
			} else {
				ClientUtils.log("topic.getChildrentIds() is null");
			}
			ClientUtils.log("updateTopicProgress: " + progress.getId() + " cardBox0: " + boxes0);
			ClientUtils.log("updateTopicProgress: " + progress.getId() + " cardBox1: " + boxes1);
			ClientUtils.log("updateTopicProgress: " + progress.getId() + " cardBox2: " + boxes2);
			ClientUtils.log("updateTopicProgress: " + progress.getId() + " cardBox3: " + boxes3);
			ClientUtils.log("updateTopicProgress: " + progress.getId() + " cardBox4: " + boxes4);
			ClientUtils.log("updateTopicProgress: " + progress.getId() + " cardBoxm: " + boxesM);
				if (LoginManager.getUserSetting().getMaxBoxNum() <= 3) {
					t += 0*boxes0 + 0.33*boxes1 + 0.67*boxes2 + 1*boxes3 + 1*boxes4 + 1*boxesM;
				}
				else if (LoginManager.getUserSetting().getMaxBoxNum() >= 4) {
					t += 0*boxes0 + 0.25*boxes1 + 0.5*boxes2 + 0.75*boxes3 + 1*boxes4 + 1*boxesM;
				}
				topicProgress = (int) ((t/childCardSize)*100);
				if (t > 0 && topicProgress < 1)
					topicProgress = 1;
		}
		if(topicProgress >= minToPass) {
			progress.setPassed(1);
			topicProgress = 100;
		}
		else 
			progress.setPassed(0);
		ClientUtils.log("New topic progress: " + topicProgress);
		progress.setProgress(topicProgress);
		progress.setLanguageId(AppConfig.LANGUAGE_ID);
		progress.setLastChildCardNum(childCardSize);
		progress.setViewNum(progress.getViewNum() + 1);
		progress.setCategoryName(topic.getName());
		progress.setParentId(topic.getParentId());
		progress.setUserId(LoginManager.getCurrentUser().getId());
		progress.setUserName(LoginManager.getCurrentUser().getDisplayName());
		progress.setAppId(AppConfig.MAIN_TOPIC_ID);
		topic.setProgress(progress);
		return progress;
	}
	
	@Override
	protected void onBackButtonPressed() {
		if(ReplySliding.getInstance().isShowing()){
			ReplySliding.getInstance().onBack();
			return;
		}else if(CommentSliding.getInstance().isShowing()){
			CommentSliding.getInstance().hide();
			return;
		}
		
		if(SlidingPanel.hideSliding()) {
			return;
		} 
		if(newBasicGameView.getStudyNewCardDialog().isLoading()){
			return;
		}
		clientFactory.getDictionarySearchDialog();
		if(DictionarySearchDialog.renderPopup.isShowing()){
			DictionarySearchDialog.renderPopup.hide();
			return;
		}
		askToQuitGame();
	}
	
	protected void onFinishGame(boolean forceQuit) {
		AudioManager.forceStopSound();
		showAdsFull();
		if(gameMode == Config.MODE_PRATICE) {
			boolean isShowResult = true;
			if(playTopics != null){
				for (Topic topic : playTopics) {
					if(topic.getType() == Config.TYPE_TOPIC_SPEAKING){
						isShowResult = false;
						break;
					}
				}
			}
			if(isShowResult){
				updateTopicsProgress();
				newBasicGameView.onGameFinished();
				for (GameObjectResult object : newBasicGameView.getGameResults()) {
					object.getGameCore().onStop();
				}
				Place previousPlace = ((BasicPlace)place).getPreviousPlace();
				if(!newBasicGameView.getCurrentGameObjects().isEmpty()
						&& newBasicGameView.getCurrentGameObjects().get(0) instanceof WritingGameObject){
					goTo(previousPlace);
				}else{
					goTo(new EndGamePlace(previousPlace, mainTopic, playTopics, newBasicGameView.getGameResults(), cards, gameMode, gameSource));
				}
			} else {
				Place previousPlace = ((BasicPlace)place).getPreviousPlace();
				goTo(previousPlace);
			}
		} else if(gameMode == Config.MODE_STUDY) {
			if(forceQuit) {
				newBasicGameView.reset();
				Place previousPlace = ((BasicPlace)place).getPreviousPlace();
				goTo(previousPlace);
			}
			else {
				Button btnPractice = new Button(Locale.get().practice());
				btnPractice.getElement().getStyle().setBorderColor(CssToken.THEME_COLOR);
				btnPractice.getElement().getStyle().setColor(CssToken.THEME_COLOR);
				btnPractice.getElement().setClassName("btnDialog");
				final Place previousPlace = ((BasicPlace)place).getPreviousPlace();
				final AlertDialogUiView dialogUiView = new AlertDialogUiView("Finished Study", null, "Back", "Continue", new ConfirmCallback() {
					
					@Override
					public void onOk() {
						goTo(previousPlace);
					}
					
					@Override
					public void onCancel() {
						clientFactory.getPlaceController().goTo(new GamePlace(previousPlace, mainTopic, playTopics,  Config.MODE_STUDY, gameSource, -1));
					}
				});
				btnPractice.addTapHandler(new TapHandler() {
					
					@Override
					public void onTap(TapEvent event) {
						List<Card> cards = new ArrayList<Card>();
						for (GameObject gameObject : newBasicGameView.getCurrentGameObjects()) {
							if(gameObject instanceof StudyObject){
								cards.add(((StudyObject)gameObject).getCard());
							}
						}
						clientFactory.getPlaceController().goTo(new GamePlace(previousPlace, mainTopic, playTopics, cards, Config.MODE_PRATICE, gameSource));
						dialogUiView.hide();
					}
				});
				if(gameSource != null && gameSource == GameSource.CATEGORY){
					dialogUiView.getBtnRight().setVisible(true);
				} else {
					dialogUiView.getBtnRight().setVisible(false);
				}
				dialogUiView.getControlPanel().add(btnPractice);
				dialogUiView.setHideOnBackgroundClick(false);
				dialogUiView.getMainPanel().setWidth(ClientUtils.getScreenWidth() * 2/3 +"px");
				dialogUiView.center();
			}
		}
	}
	
	protected void askToQuitGame() {
		String content = Locale.get().areyousurequitgame();
		if (exit) {
			try {
				if(newBasicGameView.getStudyNewCardDialog().isShowing()){
					newBasicGameView.getStudyNewCardDialog().hide();
					Place previousPlace = ((BasicPlace)place).getPreviousPlace();
					goTo(previousPlace);
					return;
				}
				onFinishGame(true);
			} catch (Exception e) {
				Place previousPlace = ((BasicPlace)place).getPreviousPlace();
				goTo(previousPlace);
			}
			
		} else {
			if(!ClientUtils.isLocalTestMode()){
				Toaster.showToast(content + " " +Locale.get().tapBackAgainToQuit(), false, 3);
			}
		}
		exit = !exit;
		exitTimer.schedule(3000);
	}

}
