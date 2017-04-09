import { Component } from '@angular/core';
import { NavController, NavParams, PopoverController } from 'ionic-angular';

import { Card } from '../../shared/models/card';
import { Setting } from '../../shared/components/setting/setting';
import { CardService } from '../../shared/services/card.service';
import { UtilService } from '../../shared/services/util.service';
import { GamePage } from '../game/game';
import { LocalStorageService } from '../../shared/services/localStorage.service';

@Component({
    selector: 'page-flashcard',
    templateUrl: 'flashcard.html',
    providers: [CardService, UtilService, LocalStorageService]
})
export class FlashCardPage {    
    public listCard: Array<Card>;
    public listCardAll: Array<Card>;
    public listClassCard: Array<any>;
    public listCardDataId: Array<string>;
    public numberQuestion: number;

    constructor(public navCtrl: NavController, public navParams: NavParams, public cardService: CardService, public utilService: UtilService, public popoverCtrl: PopoverController, public localStorageService: LocalStorageService) {
        this.listCard = [];        
        this.listClassCard = [];
        this.listCardDataId = this.navParams.get('listCardDataId');
        this.listCard = this.navParams.get('listCard');
        this.listCardAll = this.navParams.get('listCardAll');
        this.numberQuestion = this.localStorageService.getItem('numberQuestion');                
    }            

    flipCard(index) {
        if (this.listClassCard[index] == "flip") {
            this.listClassCard[index] = "";
        } else {
            this.listClassCard[index] = "flip"
        }
    }

    goPractice () {        
        this.navCtrl.push(GamePage, {
            listCardDataId: this.listCardDataId,
            listCardAll: this.listCardAll
        });
    }   

    showSetting (myEvent) {
        let popover = this.popoverCtrl.create(Setting);
        popover.present({
            ev: myEvent
        });
    }    
}
