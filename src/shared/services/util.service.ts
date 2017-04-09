import { Injectable } from '@angular/core';
import { LoadingController, Platform } from 'ionic-angular';
import { SOUND_CORRECT_GAME, SOUND_WRONG_GAME } from '../contanst/index';
import { LocalStorageService } from './localStorage.service';
// import { Toast } from '@ionic-native/toast';

@Injectable()
export class UtilService {
	public loader: any;

	constructor (private loadingCtrl: LoadingController, public platform: Platform, public localStorageService: LocalStorageService) {
		this.loader = this.loadingCtrl.create({
            content: "Please wait..."
        });			
	}	

	showLoading () {		
        this.loader.present();
	}

	hiddenLoading () {
		this.loader.dismiss();
	}

	isMobile () {
		return false;
		// return this.platform.is('mobile');
	}

	clone (data) {
		return JSON.parse(JSON.stringify(data));
	}

	getRandomItem (list: Array<any>): any{
		return list[Math.floor(Math.random() * list.length)];
	}

	// function shuffle item in array
	shuffleArray (array: Array<any>) {
      	let newArray = JSON.parse(JSON.stringify(array));
      	let m = newArray.length, t, i;
      	
      	while (m) {        	
        	i = Math.floor(Math.random() * m--);        	
        	t = newArray[m];
        	newArray[m] = newArray[i];
        	newArray[i] = t;
      	}

      	return newArray;
    }

	showToast (message) {
		if (this.isMobile()) {
			// this.toast.show(message, '5000', 'center').subscribe(
			// 	toast => {
			// 		console.log(toast);
			// 	}
			// );
		} else {
			alert(message);
		}
	}

	playSound (path: string) {
		var audio = new Audio();
		audio.src = path;
		audio.load();
		audio.play();
	}

	playSoundGame (result) {
		let path = result ? SOUND_CORRECT_GAME : SOUND_WRONG_GAME;
		if (this.localStorageService.getItem('sound')) {
			this.playSound(path);
		}
	}
}