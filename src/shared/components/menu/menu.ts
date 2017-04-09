import { Component } from '@angular/core';
import { App } from 'ionic-angular';

import { LoginPage } from '../../../pages/login/login';
import { OurappsPage } from '../../../pages/ourapps/ourapps';
import { FeedbackPage } from '../../../pages/feedback/feedback';
import { SettingPage } from '../../../pages/setting/setting';

@Component({
  	selector: 'menu',
  	templateUrl: 'menu.html'
})

export class Menu {	
	constructor(public appCtrl: App) {

	} 

	goLogin () {		
		this.appCtrl.getRootNav().push(LoginPage);
	}

	goOurApps () {
		this.appCtrl.getRootNav().push(OurappsPage);
	}

	goFeedback () {
		this.appCtrl.getRootNav().push(FeedbackPage);
	}

	goSetting () {
		this.appCtrl.getRootNav().push(SettingPage);
	}
}
