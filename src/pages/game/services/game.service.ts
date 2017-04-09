import { Injectable } from '@angular/core';

import { Card } from '../../../shared/models/card';
import { Face } from '../models/face.model';
import { Box } from '../models/box.model';
import { TrueFalseGame } from '../models/truefalse.model';
import { MatchingGame } from '../models/matching.model';
import { MultipleGame } from '../models/multiple.model';
import { SentenceGame } from '../models/sentence.model';
import { GameObject } from '../models/game.model';
import { GAME_MATCHING, GAME_TRUE_FALSE, GAME_MULTICHOICES, GAME_SENTENCES } from '../../../shared/contanst/index';
import { CardService } from '../../../shared/services/card.service';

@Injectable()
export class GameService {
	constructor (public cardService: CardService) {
		
	}	

	public genarateGame (card: Card, gameType: string, listCard: Array<Card>, listCardChild: Array<Card>): GameObject {
		switch (gameType) {
			case GAME_MATCHING:				
				return this.convertMatchingGame(card, listCard);
			case GAME_TRUE_FALSE:
				return this.convertTrueFalseGame(card, listCard);
			case GAME_MULTICHOICES:
				return this.convertMultipleGame(card, listCard);			
			default:
				return this.convertSentenceGame(card, listCard, listCardChild);
		}
	}

	public convertMultipleGame  (card: Card, listCard: Array<Card>): MultipleGame {
		// Check card have mutltichoices. If not then genarate mutltichoices game		
		let listAnswer: Array<Face> = [];
		let listCorrectAnswer: Array<Face> = [];
		if (card.multiChoices.length) {
			card.multiChoices.map((multi, index) => {
				listAnswer.push(new Face(index, multi, "", "", "", card.backLanguage));
			})
			
			let backTexts = card.backTexts;
			if (backTexts.length) {
				backTexts.map((backtext, index) => {
					listCorrectAnswer.push(new Face(index, backtext, "", "", "", card.backLanguage))
					listAnswer.push(new Face(index, backtext, "", "", "", card.backLanguage));
				})	
			}						
		} else {
			let index = 0;
			while (listAnswer.length < 3) {
				let item = listCard[index];
				if (item.frontText != card.frontText) {
					listAnswer.push(new Face(index, item.backText, "", "", "", item.backLanguage));					
				}												

				index ++;
			}								
		}

		listCorrectAnswer.push(new Face(100, card.backText, "", "", "", card.backLanguage))		
		listAnswer.push(new Face(100, card.backText, "", "", "", card.backLanguage));
		listAnswer = this.shuffleArray(listAnswer);
		let question = new Face(1, card.frontText, card.frontSound, card.frontSound, card.frontHint, card.frontLanguage);

		return new MultipleGame(this.randomNumber(), GAME_MULTICHOICES, question, listAnswer, listCorrectAnswer);		
	}

	public convertTrueFalseGame (card: Card, listCard: Array<Card>): TrueFalseGame {		
		let isCorrect = this.getRandomItem([true, false]);
		if (isCorrect) {			
			return new TrueFalseGame(
				1,
				GAME_TRUE_FALSE,
				new Face(1, card.frontText, card.frontSound, card.frontImage, card.frontHint, card.frontLanguage),
				new Face(2, card.backText, card.backSound, card.backImage, card.backHint, card.backLanguage),								
				new Face(3, card.backText, card.backSound, card.backImage, card.backHint, card.backLanguage),
				isCorrect
			)			
		} else {
			let fakeCard: Card = this.getRandomItem(listCard);
			while (fakeCard.frontText == card.frontText) {
				fakeCard = this.getRandomItem(listCard);
			}			
			
			return new TrueFalseGame(
				1,
				GAME_TRUE_FALSE,
				new Face(1, card.frontText, card.frontSound, card.frontImage, card.frontHint, card.frontLanguage),
				new Face(2, fakeCard.backText, fakeCard.backSound, fakeCard.backImage, fakeCard.backHint, fakeCard.backLanguage),								
				new Face(3, card.backText, card.backSound, card.backImage, card.backHint, card.backLanguage),
				isCorrect
			)
		}				
	}

	public convertMatchingGame (card: Card, listCard: Array<Card>): MatchingGame {
		let listBox: Array<Box> = [];
		listBox.push(new Box(
			0,
			new Face(0, card.frontText, card.frontSound, card.frontImage, card.frontHint, card.frontLanguage),
			card.backText,
			'Q'
		))

		listBox.push(new Box(
			1, 
			new Face(1, card.backText, card.backSound, card.backImage, card.backHint, card.backLanguage), 
			card.frontText,
			'A'
		))					


		// Get random card to create four box of macthing game
		let randomCard = this.getRandomItem(listCard);
		while (randomCard.id == card.id) {
			randomCard = this.getRandomItem(listCard);
		}
		listBox.push(new Box(
			2, 
			new Face(2, randomCard.frontText, randomCard.frontSound, randomCard.frontImage, randomCard.frontHint, randomCard.frontLanguage), 
			randomCard.backText,
			'Q'
		))

		listBox.push(new Box(
			3, 
			new Face(3, randomCard.backText, randomCard.backSound, randomCard.backImage, randomCard.backHint, randomCard.backLanguage), 
			randomCard.frontText,
			'A'
		))				

		return new MatchingGame(1, GAME_MATCHING, this.shuffleArray(listBox));		
	}

	public convertSentenceGame (card: Card, listCard: Array<Card>, listCardChild: Array<Card>): SentenceGame {
		let sentence = new Face(1, card.frontText, card.frontSound, card.frontImage, card.frontHint, card.frontLanguage);
		let listQuestion: Array<MultipleGame> = [];
		listCardChild.map((item) => {
			if (item.parentId == card.id) {
				listQuestion.push(this.convertMultipleGame(item, listCard))
			}			
		});
		
		return new SentenceGame(1, GAME_SENTENCES, sentence, listQuestion)								
	}

	public getRandomGame (card: Card):string {
		// let listGame = [GAME_MATCHING, GAME_TRUE_FALSE, GAME_MULTICHOICES, GAME_SENTENCES];
		let listGame = [GAME_MATCHING, GAME_TRUE_FALSE, GAME_MULTICHOICES];
		if (card.hasChild) {
			return GAME_SENTENCES;
		} 

		if (card.multiChoices.length) {
			return GAME_MULTICHOICES;
		}
		
		return this.getRandomItem(listGame);
	}

	// function shuffle item in array
	private shuffleArray (array: Array<any>) {
      	let newArray = JSON.parse(JSON.stringify(array));
      	let m = newArray.length, t, i;
      	
      	while (m) {        	
        	i = Math.floor(Math.random() * m--);        	
        	t = newArray[m];
        	newArray[m] = newArray[i];
        	newArray[i] = t;
      	}

      	return newArray;
    }

	private getRandomItem (list: Array<any>): any{
		return list[Math.floor(Math.random() * list.length)];
	}

	private randomNumber (): number {
		return Math.floor((Math.random() * 1000000) + 1);
	}
}