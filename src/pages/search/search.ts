import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

/*
  Generated class for the Search page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  	selector: 'page-search',
  	templateUrl: 'search.html'
})
export class SearchPage {
	public searchType: boolean;
	public importType: boolean;	

  	constructor(public navCtrl: NavController, public navParams: NavParams) {
  		this.searchType = true;
  		this.importType = false;  		
  	}

  	chooseSearch () {
  		this.importType = this.searchType ? false : true;
  	}  	

  	chooseImport () {
  		this.searchType = this.importType ? false : true;
  	}
}
