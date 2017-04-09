import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

/*
  Generated class for the Exam page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-exam',
  templateUrl: 'exam.html'
})
export class ExamPage {

	public listExam: Array<any>;

  	constructor(public navCtrl: NavController, public navParams: NavParams) {
  		this.listExam = [];
  	}

  	ngOnInit () {
  		this.listExam = [
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
