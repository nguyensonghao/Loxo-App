package com.koolsoft.littlezeros.client.activities.game.gamecore;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollEndEvent;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollEndEvent.Handler;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.touch.TouchPanel;
import com.koolsoft.littlezeros.client.ClientUtils;
import com.koolsoft.littlezeros.client.CssToken;
import com.koolsoft.littlezeros.client.activities.basic.BasicViewImpl.Layout;
import com.koolsoft.littlezeros.client.activities.game.GameSetting;
import com.koolsoft.littlezeros.client.activities.game.ResultPanel;
import com.koolsoft.littlezeros.client.activities.game.widget.CardViewer;
import com.koolsoft.littlezeros.client.activities.game.widget.GameProgressWidget;
import com.koolsoft.littlezeros.client.resource.BHClientBundleBaseTheme;
import com.koolsoft.littlezeros.client.view.BHTouchImage;
import com.koolsoft.littlezeros.client.view.KSButtonBar;

public class GameCoreView extends TouchPanel implements IGameCoreView{
	
	protected HorizontalPanel headerPanel = new HorizontalPanel();
	protected ScrollPanel mainViewScroll = new ScrollPanel();
	protected FlexTable gameViewPanel = new FlexTable();
	protected KSButtonBar buttonBar = new KSButtonBar();
	protected ResultPanel resultPanel = new ResultPanel();
	protected Layout mainLayout = null;
	protected HTML gameInfoHtml = new HTML();
	protected BHTouchImage bookmarkButton = new BHTouchImage(BHClientBundleBaseTheme.IMPL.getBHMGWTClientBundle().bookmarkOff(), 
				BHClientBundleBaseTheme.IMPL.getBHMGWTClientBundle().bookmarkOn());
	protected CardViewer questionView;
	protected Widget gameProgressWidget;
	protected GameObject gameObject;
	protected int buttonBar_height = 50;
	protected int header_height = 38;
	protected int cardScreenHeight = 0;
	protected int cardScreenWidth = 0;
	protected GameSetting gameSetting;
	private int bookmarkWidth = 25;
	private int bookmarkHeight = 20;

	
	public GameCoreView(Widget gameProgressWidget, Layout layout) {
		super();
		this.gameProgressWidget = gameProgressWidget;
		this.mainLayout = layout;
		this.add(headerPanel);
		this.add(mainViewScroll);
		this.add(buttonBar);
		this.add(resultPanel);
		mainViewScroll.setWidget(gameViewPanel);
		mainViewScroll.getElement().setId("mainViewScroll");
		mainViewScroll.getElement().getStyle().setBackgroundColor("white");
		mainViewScroll.setShowHorizontalScrollBar(false);
		mainViewScroll.setShowVerticalScrollBar(false);
		mainViewScroll.setBounce(false);
		resultPanel.getElement().getStyle().setPosition(Position.ABSOLUTE);
		resultPanel.getElement().getStyle().setBottom(0, Unit.PX);
		this.setWidth("100%");
		gameViewPanel.setWidth("100%");
		bookmarkButton.setSize(bookmarkWidth + "px", bookmarkHeight + "px");
		bookmarkButton.getElement().getStyle().setFloat(Float.RIGHT);
		headerPanel.getElement().getStyle().setBackgroundColor("#e8f6fc");
		setSize();
		refreshView();
		mainViewScroll.addScrollEndHandler(new Handler() {
			
			@Override
			public void onScrollEnd(ScrollEndEvent event) {
				mainViewScroll.refresh();
			}
		});
	}
	
	public void setSize(){
		buttonBar.setHeight(buttonBar_height);
		cardScreenHeight = ClientUtils.getScreenHeight() - buttonBar_height - header_height - ClientUtils.getPaddingBottom();
		cardScreenWidth = ClientUtils.getScreenWidth() - 10;
	}
	
	@Override
	public void showHeader() {
		headerPanel.clear();
		headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		headerPanel.setWidth("100%");
		headerPanel.add(gameInfoHtml);
		gameInfoHtml.getElement().getStyle().setProperty("minWidth", "50px");
		gameInfoHtml.getElement().getStyle().setTextAlign(TextAlign.CENTER);
		gameInfoHtml.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		if(gameProgressWidget != null){
			headerPanel.add(gameProgressWidget);
		}
		headerPanel.add(bookmarkButton);
	}
	
	@Override
	public void updateProgressWidgetWidth(){
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				if(gameProgressWidget != null){
					int widthProgress = cardScreenWidth - gameInfoHtml.getOffsetWidth() - bookmarkButton.getOffsetWidth();
					gameProgressWidget.setWidth(widthProgress + "px");
					((GameProgressWidget)gameProgressWidget).refresh();
					headerPanel.setCellWidth(headerPanel, gameInfoHtml.getOffsetWidth() + "px");
					headerPanel.setCellWidth(gameProgressWidget, widthProgress + "px");
					headerPanel.setCellWidth(gameProgressWidget, bookmarkButton.getOffsetWidth() + "px");
				}
			}
		});
	}

	public void showButtonBar() {
		buttonBar.setWidth("100%");
		buttonBar.setVisible(false);
		buttonBar.setHeight(buttonBar_height + "px");
		buttonBar.setStyleName(CssToken.BUTTONBAR_GAME);
		buttonBar.gethPanel().setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		buttonBar.gethPanel().setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	@Override
	public void onStart(GameObject gameObject, GameSetting gameSetting) {
		this.gameObject = gameObject;
		this.gameSetting = gameSetting;
	}

	@Override
	public void onAnswer(int isCorrect, String playerName, int score, int gameType) {
		resultPanel.getElement().setId("panelResult");
		resultPanel.showResultPanel(isCorrect, playerName, score, gameType);
	}

	@Override
	public void onStop(GameObject gameObject) {
		resultPanel.hide();
		buttonBar.removeFromParent();
	}

	@Override
	public void showHideKeyboard(boolean isShow) {
		
	}
	
	@Override
	public void showDictionaryMode(boolean isDictionaryMode) {
		questionView.refreshView(isDictionaryMode, false);
	}
	
	@Override
	public ResultPanel getResultPanel() {
		return resultPanel;
	}
	
	@Override
	public CardViewer getQuestionView() {
		return questionView;
	}
	
	@Override
	public void refreshView() {
		mainViewScroll.setHeight(cardScreenHeight + "px");
		if(mainViewScroll.iterator().hasNext()){
			ClientUtils.log("refreshView xxxx");
			mainViewScroll.refresh();
		}
	}
	
	@Override
	public TouchPanel asWidget() {
		return this;
	}

	@Override
	public void stopAudio() {
		if (questionView != null)
			questionView.stopSound();
	}
	
	@Override
	public BHTouchImage getBookmarkButton() {
		return this.bookmarkButton;
	}

	@Override
	public void showCorrectAnswer() {
	}

	@Override
	public void updateScore(String score) {
		gameInfoHtml.setHTML("<font size='2'>" + score + "</font>");
	}
	
	@Override
	public HTML getGameInfoHtml() {
		return gameInfoHtml;
	}
	
	@Override
	public HorizontalPanel getHeaderPanel() {
		return headerPanel;
	}

	@Override
	public Widget gameProgressWidget() {
		return gameProgressWidget;
	}

	@Override
	public ScrollPanel getMainViewScroll() {
		return mainViewScroll;
	}
}