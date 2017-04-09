import { Component } from '@angular/core';
import { ViewController } from 'ionic-angular';

/*
  Generated class for the Upgrade page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  	selector: 'page-upgrade',
  	templateUrl: 'upgrade.html'
})
export class UpgradeModal {

  	constructor(public viewCtrl: ViewController) {

  	}

  	closeModal () {
  		this.viewCtrl.dismiss();
  	}

}
