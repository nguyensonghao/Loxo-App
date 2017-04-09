import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { GameObject } from './models/game.model';
import { Card } from '../../shared/models/card';
import { CardService } from '../../shared/services/card.service';
import { UtilService } from '../../shared/services/util.service';
import { GameService } from './services/game.service';

@Component({
  	selector: 'page-game',
  	templateUrl: 'game.html',
  	providers: [CardService, UtilService, GameService]
})

export class GamePage {		
	private index: number;			
	public loadDone: Boolean;
	public listCard: Array<Card>;
	public listCardChild: Array<Card>;	
	public currentGame: string;
	public showAnswer: string;
	public resultAnswer: boolean;
	public showResultGame: boolean;	    
	public listCardDataId: Array<string>;
	public resultGame: any;
	public history: Array<any>;
	public listGame: Array<GameObject>;
	public listCardIdHasChild: Array<string>;
	public exitGame: boolean;

	constructor(public navCtrl: NavController, public navParams: NavParams, public cardService: CardService, public gameService: GameService, public utilService: UtilService) {
		this.listCardDataId = this.navParams.get("listCardDataId")
		this.listCard = this.navParams.get("listCardAll");
		this.listCardChild = [];
		this.listCardIdHasChild = [];		
		this.loadDone = false;
		this.resultAnswer = false;
		this.showResultGame = false;
		this.showAnswer = '';
		this.history = [];	
		this.listGame = [];			
		this.exitGame = false;
  	}  	

  	ngOnInit () {				
  		this.listCard = this.utilService.shuffleArray(this.listCard);				
		this.index = 0 ;																

		// Load list card child of sentence game
		this.utilService.showLoading();
		this.listCard.map((card) => {
			if (card.hasChild) {
				this.listCardIdHasChild.push(card.id.toString());				
			}
		})		

		this.cardService.getListCardChildById(this.listCardIdHasChild).then((data) => {
			this.listCardChild = data;
			this.genarateGame();
			// this.currentGame = this.listGame[0].gameType;						
			this.utilService.hiddenLoading();	
			this.loadDone = true;
		});
  	}

	ionViewCanLeave () {
		if (!this.exitGame) {
			this.utilService.showToast('Do you want to quit this game. Tap back again to quit');
			this.exitGame = true;
			return false;
		}		

		return true;
	}

  	answerQuestion (answer: Object) {		
		this.resultGame = answer['data'];
		this.resultAnswer = answer['result'];
		this.currentGame = answer['gameType'];
		this.showAnswer = 'show-box';				
		this.utilService.playSoundGame(this.resultGame);

		// Save history user answer
		this.history.push({
			index: this.index,
			result: this.resultAnswer
		})						
	}

	nextCard () {
		this.showAnswer = '';
		// End game and show result
		if (this.history.length == this.listGame.length) {
			this.showResultGame = true;
			return;
		}			

		// Next Game 
		for (var i = this.index + 1; i < this.listGame.length; i++) {
			let check = this.history.find((history) => {
				return history['index'] == i;
			})

			if (!check) {
				this.index = i;			
				this.currentGame = this.listGame[i].gameType;	
				return;
			}			
		}		

		for (var i = 0; i < this.index; i++) {
			let check = this.history.find((history) => {
				return history['index'] == i;
			})

			if (!check) {
				this.index = i;		
				this.currentGame = this.listGame[i].gameType;		
				return;
			}
		}
	}	  		

	changePage (page: number) {
		this.index = page;		
	}	

	genarateGame () {
		this.listCard.map((card) => {
			let gameType = this.gameService.getRandomGame(card);
			this.listGame.push(this.gameService.genarateGame(card, gameType, this.listCard, this.listCardChild));
		})		
	}
}
