import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { RankService } from '../../../../shared/services/rank.service';
import { Rank } from '../../../../shared/models/rank';

@Component({
  	selector: 'page-ranking',
  	templateUrl: 'ranking.html',
    providers: [RankService]
})
export class RankingPage {
	public listRank: Array<Rank>;

  	constructor(public navCtrl: NavController, public navParams: NavParams, public rankService: RankService) {
  		this.listRank = [];
  	}  

  	ngOnInit () {
  		this.rankService.getList().subscribe(data => {
            this.listRank = data;
        })  
  	}
}
