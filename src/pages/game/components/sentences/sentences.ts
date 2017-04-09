import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NavController, NavParams, AlertController } from 'ionic-angular';

import { Card } from '../../../../shared/models/card';
import { SentenceGame } from '../../models/sentence.model';

@Component({
  	selector: 'sentences-game',
  	templateUrl: 'sentences.html'	
})

export class SentencesBox {
	@Input() game: SentenceGame;	
	@Output() choiceAnswer = new EventEmitter<Object>();	
	public hiddenListQuestion: boolean;
	public listAnswer: Array<any>;	
	public numberAnswered: number;
	public numberAnsweredCorrect: number;

  	constructor(public navCtrl: NavController, public navParams: NavParams, public alertCtrl: AlertController) {
		this.hiddenListQuestion = false;
		this.listAnswer = [];	
		this.numberAnswered = 0;
		this.numberAnsweredCorrect = 0;	
	}

	showQuestion (status: boolean) {
		this.hiddenListQuestion = !status;
	}	

	choiceResult (answer, question) {
		if (this.game.endGame)
			return;
			
		this.numberAnswered ++;
		this.listAnswer[question.id] = 'none-click';		
		let check = question.correctAnswer.find((item) => {
			return item.text == answer.text
		});		

		if (check) {
			this.listAnswer[answer.text] = 'correct-answer';
			this.numberAnsweredCorrect ++;
		} else {
			this.listAnswer[answer.text] = 'wrong-answer';
			question.correctAnswer.map((item) => {
				this.listAnswer[item.text] = 'correct-answer';
			});		
		}		 
	}	

	// Function mark all correct answer when use submit game
	showAllAnswerCorrect () {
		this.game.listQuestion.map((question) => {
			question.correctAnswer.map((answer) => {
				this.listAnswer[answer.text] = 'correct-answer';
			})
		})		

		this.emitEndGame();	
	}

	emitEndGame () {
		this.game.endGame = true;
		let result = this.numberAnsweredCorrect == this.game.listQuestion.length;
		this.choiceAnswer.emit({
			result: result,
			gameType: this.game.gameType,
			data: {
				totalQuestion: this.game.listQuestion.length,
				numberCorrect: this.numberAnsweredCorrect
			}			
		});
	}

	// Function return icon of answer when use choice answer
	genIconName (className): string {
		if (!className)
			return 'md-radio-button-off';

		if (className == 'correct-answer')
			return 'md-checkmark-circle';

		return 'md-close-circle'
	}

	submit () {
		if (this.numberAnswered < this.game.listQuestion.length) {
			this.showConfirm(this.game.listQuestion.length - this.numberAnswered);
		} else {
			this.emitEndGame();
		}
	}

	showConfirm(numberQuestion: number) {
    	let confirm = this.alertCtrl.create({
      		title: 'Warning',
      		message: 'You have ' + numberQuestion + ' questions that are not answered yet, do you still want to submit',
      		buttons: [
        		{
          			text: 'Ok',
          			handler: () => {
            			this.showAllAnswerCorrect();
          			}
        		},
        		{
          			text: 'Cancel',
          			handler: () => {
            			console.log('Agree clicked');
          			}
        		}
      		]
    	});

    	confirm.present();
  }
}
