package com.koolsoft.littlezeros.client.activities.game.gamecore;

import java.util.List;

import com.koolsoft.littlezeros.client.activities.game.study.StudyObject;
import com.koolsoft.littlezeros.shared.interfacemodel.IBasic;
import com.koolsoft.littlezeros.shared.model.CardProgress;
import com.koolsoft.littlezeros.shared.model.CardStatistic;


public interface GameObject extends IBasic{
	
	@Override
	public Long getId();
	public Long getParentId();
	public Long getTopicId();
	public int getGameObjectType();
	public int getIncorrectScore();
	public int getCorrectScore();
	public void setIncorrectScore(int score);
	public void setCorrectScore(int score);
	public void setBox(int box);
	public int getBox();
	public CardProgress getProgress();
	public int getLevel();
	public String getTopicName();
	public void setIndex(int index);
	public int getIndex();
	public Long getCategoryId();
	public int getSkill();
	public int getDatabase();
	public boolean isLoadedData();
	public CardStatistic getCardStatistic();
	public String getAnswerKey();
	public String getAnswerHint();
	public String getPartOfSpeech();
	public List<Long> getCardIds();
	public List<StudyObject> getStudyObject();
}
