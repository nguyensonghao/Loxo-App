import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

/*
  Generated class for the Content page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-content',
  templateUrl: 'content.html'
})
export class ContentPage {

	constructor(public navCtrl: NavController, public navParams: NavParams) {}

	public pieChartLabels:string[] = ['Download Sales', 'In-Store Sales', 'Mail Sales'];
	public pieChartData:number[] = [300, 500, 100];
	public pieChartType:string = 'pie';
	 
	  // events
	public chartClicked(e:any) {
	    console.log(e);
	}
	 
	public chartHovered(e:any) {
	    console.log(e);
	}

}
