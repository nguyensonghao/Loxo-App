import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { RegisterPage } from '../register/register';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {

  	constructor(public navCtrl: NavController, public navParams: NavParams) {

  	}

  	goRegister () {
  		this.navCtrl.push(RegisterPage);
  	}
}
