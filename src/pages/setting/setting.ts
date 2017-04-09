import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { LocalStorageService } from '../../shared/services/localStorage.service';

@Component({
  	selector: 'page-setting',
  	templateUrl: 'setting.html',
	providers: [LocalStorageService]
})
export class SettingPage {
	public soundSetting: boolean;
	public publicProfileSetting: boolean;
	public notifySetting: boolean;
	public timeNotify: string;
	public listTime: Array<string>;

  	constructor(public navCtrl: NavController, public navParams: NavParams, public localStorageService: LocalStorageService) {
		this.listTime = [];
		for (var i = 0; i < 24; i++) {
			let time: string;			 
			if (i < 10) {
				time = '0' + i.toString() + ':00 AM';
			} else if (i < 12) {
				time = i.toString() + ':00 AM'
			} else {
				time = i.toString() + ':00 PM';
			}

			this.listTime.push(time);
		}
	}  

	ngOnInit () {		
		let soundSetting = this.localStorageService.getItem('sound');
		this.soundSetting = soundSetting != null ? soundSetting : false;
		let publicProfileSetting = this.localStorageService.getItem('profile');
		this.publicProfileSetting = publicProfileSetting != null ? publicProfileSetting : false;
		let notifySetting = this.localStorageService.getItem('notify');
		this.notifySetting = notifySetting != null ? notifySetting : false;
		let timeNotify = this.localStorageService.getItem('timeNotify');
		this.timeNotify = timeNotify != null ? timeNotify : '00:00 AM';
	}

	changeSoundSetting () {
		this.localStorageService.setItem('sound', this.soundSetting);
	}

	changePublicProfileSetting () {
		this.localStorageService.setItem('profile', this.publicProfileSetting);
	}

	changeNotifySetting () {
		this.localStorageService.setItem('notify', this.notifySetting);
	}

	changeTimeNotify () {		
		this.localStorageService.setItem('timeNotify', this.timeNotify);
	}
}
