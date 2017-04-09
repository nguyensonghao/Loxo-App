import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { LoginPage } from '../login/login';

@Component({
  selector: 'page-register',
  templateUrl: 'register.html'
})
export class RegisterPage {

	constructor(public navCtrl: NavController, public navParams: NavParams) {

	}

	goLogin () {
		this.navCtrl.push(LoginPage);
	}

}
