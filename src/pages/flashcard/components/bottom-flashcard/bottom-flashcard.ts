import { Component, Input } from '@angular/core';
import { NavController, NavParams, AlertController } from 'ionic-angular';
import { TextToSpeech } from '@ionic-native/text-to-speech';

import { Card } from '../../../../shared/models/card';
import { DecodePipe } from '../../../../shared/pipes/decode.pipe';

@Component({
  	selector: 'bottom-flashcard',
  	templateUrl: 'bottom-flashcard.html',
	providers: [TextToSpeech, DecodePipe]
})
export class BottomFlashcard {
	@Input() card: Card;
	@Input() type: string;
	public soundIcon: string;

  	constructor(public navCtrl: NavController, public navParams: NavParams, public alertCtrl: AlertController, public tts: TextToSpeech, public decodePipe: DecodePipe) {
		this.soundIcon = 'md-volume-down';
  	}

	showHint (event, card: Card) {
        event.stopPropagation();

        let alert = this.alertCtrl.create({
        title: 'Hint',
            subTitle: card.frontHint,
            buttons: ['OK']
        });
        alert.present();
    } 


	sound (event, card: Card) {
		event.stopPropagation();
				
		let stringSound = this.type == 'front' ? card.frontText : card.backText;		
		this.soundIcon = 'md-volume-up';		
		this.tts.speak(this.decodePipe.transform(stringSound))
		.then(() => {
			this.soundIcon = 'md-volume-down';
		}).catch((reason: any) => {
			console.log(reason);
			this.soundIcon = 'md-volume-down';
		});
	}
}
