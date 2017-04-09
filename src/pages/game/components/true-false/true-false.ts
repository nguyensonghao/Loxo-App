import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { TrueFalseGame } from '../../models/truefalse.model';

@Component({
  	selector: 'true-false-game',
  	templateUrl: 'true-false.html'    
})
export class TrueFalseBox {
	@Input() game: TrueFalseGame;	
	@Output() choiceAnswer = new EventEmitter<Object>();
	public box: string;	
	public correctIcon: string;	

  	constructor(public navCtrl: NavController, public navParams: NavParams) {
		this.correctIcon = '';
  	}

  	choice (answer: boolean) {		
		this.game.endGame = true;	
		this.box = answer == this.game.isCorrect ? 'box-correct' : 'box-wrong';
		this.correctIcon = answer == this.game.isCorrect ? 'icon-correct' : '';							
  		this.choiceAnswer.emit({
			result: answer == this.game.isCorrect,
			gameType: this.game.gameType,
			data: this.game.correctAnswer.text
		});
  	}
}
