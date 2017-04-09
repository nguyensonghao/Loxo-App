import { Component } from '@angular/core';
import { ViewController } from 'ionic-angular';
import { LocalStorageService } from '../../services/localStorage.service';

@Component({
  	selector: 'setting',
  	templateUrl: 'setting.html',
	providers: [LocalStorageService]
})

export class Setting {
	public numberQuestion: number;
	public questionPriority: number;
	public sortQuestion: number;
	public listen: boolean;
	public sound: boolean;
	public gameSetting: Object;

  	constructor(public viewCtrl: ViewController, public localStorageService: LocalStorageService) {		
		this.numberQuestion = 5;
		this.questionPriority = 0;
		this.sortQuestion = 0;
		this.listen = false;
		this.sound = false;
		this.gameSetting = {};
	}

	ngOnInit () {
		// Get init value of select option from localstorage 
		let numberQuestion = this.localStorageService.getItem('numberQuestion');		
		this.numberQuestion = numberQuestion ? parseInt(numberQuestion) : 5;
		let questionPriority = this.localStorageService.getItem('questionPriority');		
		this.questionPriority = questionPriority ? parseInt(questionPriority) : 0;
		let sortQuestion = this.localStorageService.getItem('sortQuestion');		
		this.sortQuestion = sortQuestion ? parseInt(sortQuestion) : 0;
		let listen = this.localStorageService.getItem('listen');
		this.listen = listen != null ? listen : false;
		let sound = this.localStorageService.getItem('sound');		
		this.sound = sound != null ? sound : false;
		let gameSetting = this.localStorageService.getItem('gameSetting');
		this.gameSetting = gameSetting ? gameSetting : {};		
	}

  	close() {
    	this.viewCtrl.dismiss();
	}	

	changeNumberQuestion () {
		this.localStorageService.setItem('numberQuestion', this.numberQuestion);
	}

	changeQuestionPriority () {
		this.localStorageService.setItem('questionPriority', this.questionPriority);
	}

	changeSortQuestion () {
		this.localStorageService.setItem('sortQuestion', this.sortQuestion);
	}

	changeListen () {
		this.localStorageService.setItem('listen', this.listen);
	}

	changeSound () {
		this.localStorageService.setItem('sound', this.sound);
	}

	changeGameSetting () {
		this.localStorageService.setItem('gameSetting', this.gameSetting);
	}
}
