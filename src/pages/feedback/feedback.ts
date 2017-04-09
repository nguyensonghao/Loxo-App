import { Component } from '@angular/core';
import { NavController, NavParams , ToastController } from 'ionic-angular';

@Component({
  	selector: 'page-feedback',
  	templateUrl: 'feedback.html'
})
export class FeedbackPage {
	public message: string;
	public email: string;

  	constructor(public navCtrl: NavController, public navParams: NavParams, public toastCtrl: ToastController) {

  	}  

	ngOnInit () {
		this.message = '';
		this.email = '';
	}

	send () {		
		if (!this.message || !this.email) {			
			let toast = this.toastCtrl.create({
				message: 'Email and message must be not empty',
				duration: 2000
			});

			toast.present();
			return;
		}		

		if (!this.validateEmail(this.email)) {
			let toast = this.toastCtrl.create({
				message: 'Email is invalid',
				duration: 1000
			});

			toast.present();
			return;
		}
	}

	cancel () {
		this.email = '';
		this.message = '';
	}

	private validateEmail (email: string): boolean {
		var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		return re.test(email);
	}
}
