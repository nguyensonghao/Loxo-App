import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { Card } from '../../../../shared/models/card';
import { Box } from '../../models/box.model';
import { MatchingGame } from '../../models/matching.model';

@Component({
  	selector: 'matching-game',
  	templateUrl: 'matching.html',    
})
export class MatchingBox {
    @Input() game: MatchingGame;
    @Output() choiceAnswer = new EventEmitter<Object>();        
    public currentBox: Box;
    public listClass: Array<any>;
    public status: boolean;
    public running: boolean;
    public correctMatching: number;
    public numberPressCard: number;

  	constructor(public navCtrl: NavController, public navParams: NavParams) {
        this.status = true;  		
        this.listClass = [];
        this.running = false;
        this.correctMatching = 0;
        this.numberPressCard = 0;    
  	}      

    emitGame (status) {
        this.game.endGame = true;
        this.choiceAnswer.emit({                            
            result: status,
            gameType: this.game.gameType,
            data: null
        })

        if (!status) 
            this.showResult();        
    }  

    showResult () {
        let hasCardCorrect = false;
        this.game.listBox.map((box) => {
            let check = this.game.listBox.find((item) => {
                return item.correctAnswer == box.box.text;
            })

            if (this.listClass[check.id] == 'card-correct-2' || this.listClass[check.id] == 'card-correct') {
                this.listClass[box.id] = this.listClass[check.id];
            } else {
                this.listClass[box.id] = hasCardCorrect ? 'card-correct-2' : 'card-correct';
                hasCardCorrect = true;
            }            
        })
    }

    clickBox (answer: Box) {                        
        if (this.listClass[answer.id] == 'card-correct' || this.running || this.game.endGame)
            return;

        this.game.numberChoice --;
        if (this.status) {
            this.currentBox = answer;
            this.listClass[answer.id] = "active-match";
            // Wrong game if numberChoice = 0
            if (!this.game.numberChoice)
                this.emitGame(false);              
        } else {
            if (answer.correctAnswer == this.currentBox.box.text) {                
                this.correctMatching ++;
                if (this.correctMatching == 2) {
                    this.listClass[this.currentBox.id] = "card-correct-2";
                    this.listClass[answer.id] = "card-correct-2";
                    this.emitGame(true);                                        
                } else {
                    this.listClass[this.currentBox.id] = "card-correct";
                    this.listClass[answer.id] = "card-correct";
                    if (!this.game.numberChoice)
                        this.emitGame(false);
                }                
            } else {
                if (!this.game.numberChoice) {
                    this.emitGame(false);
                    return;
                }

                this.listClass[this.currentBox.id] = "card-fail";
                this.listClass[answer.id] = "card-fail";
                this.running = true;
                setTimeout(() => {
                    this.listClass[this.currentBox.id] = "";
                    this.listClass[answer.id] = "";
                    this.running = false;
                }, 600);
            }
        }   
        
        this.status = !this.status;        
    }
}
