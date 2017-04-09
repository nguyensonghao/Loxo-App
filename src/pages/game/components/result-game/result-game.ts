import { Component, Input } from '@angular/core';
import { NavController, NavParams, App } from 'ionic-angular';


@Component({
    selector: 'result-game',
  	templateUrl: 'result-game.html'
})

export class ResultGame {
	@Input() history: Array<any>;
	public numberCorrect: number;
	public totalQuestion: number;

  	constructor(public navCtrl: NavController, public navParams: NavParams, public appCtrl: App) {
		
	}

	ngOnInit () {
		this.numberCorrect = 0;			
		this.totalQuestion = this.history.length;	
		this.history.map((item) => {
			if (item.result)
				this.numberCorrect ++;
		})
	}	

	goBack () {
		this.appCtrl.getRootNav().pop();
	}
}
