import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';


@Component({
  	selector: 'page-language',
  	templateUrl: 'language.html'
})
export class LanguagePage {
	public listLanguage: Array<any>;

  	constructor(public navCtrl: NavController, public navParams: NavParams) {
  		this.listLanguage = [];
  	}

  	ngOnInit () {
  		this.listLanguage = [
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			},
  			{
  				name: "Vietnamese",
  				flag: ""
  			}
  		]
  	}
}
