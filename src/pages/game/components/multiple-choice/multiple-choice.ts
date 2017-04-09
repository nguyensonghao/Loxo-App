import { Component, Output, Input, EventEmitter } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { Card } from '../../../../shared/models/card';
import { MultipleGame } from '../../models/multiple.model';

@Component({
  	selector: 'multiple-choice-game',
  	templateUrl: 'multiple-choice.html'    
})

export class MultipleChoiceBox {
	@Input() game: MultipleGame;
	@Output() choiceAnswer = new EventEmitter<Object>();    
	public answer: string;
	public listAnswer: Array<any>;

  	constructor(public navCtrl: NavController, public navParams: NavParams) {		
  		this.listAnswer = [];		
  	}    	

	// Function return icon of answer when use choice answer
	genIconName (className): string {
		if (!className)
			return 'md-radio-button-off';

		if (className == 'correct-answer')
			return 'md-checkmark-circle';

		return 'md-close-circle'
	}

  	choiceResult (answer, question) {
		if (this.game.endGame)
			return;	
				
		let check = question.correctAnswer.find((item) => {
			return item.text == answer.text
		});		

		let correctAnswer: string;
		if (check) {
			correctAnswer = answer.text;
			this.listAnswer[answer.text] = 'correct-answer';			
		} else {
			this.listAnswer[answer.text] = 'wrong-answer';
			question.correctAnswer.map((item) => {
				correctAnswer = item.text;
				this.listAnswer[item.text] = 'correct-answer';
			});		
		}

		this.choiceAnswer.emit({
			result: check ? true : false,
			gameType: this.game.gameType,
			data: correctAnswer
		})
		
		this.game.endGame = true;
  	}
}
