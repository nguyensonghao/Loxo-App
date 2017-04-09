import { Component } from '@angular/core';
import { NavController, NavParams, PopoverController } from 'ionic-angular';

import { FlashCardPage } from '../flashcard/flashcard';
import { GamePage } from '../game/game';
import { Setting } from '../../shared/components/setting/setting';
import { UtilService } from '../../shared/services/util.service';
import { CardService } from '../../shared/services/card.service';
import { CardDataService } from '../../shared/services/card-data.service';
import { Card } from '../../shared/models/card';
import { CardData } from '../../shared/models/card-data';
import { Topic } from '../../shared/models/topic';
import { NUMBER_ITEM } from '../../shared/contanst/index';


@Component({
    selector: 'page-card',
    templateUrl: 'card.html',
    providers: [UtilService, CardService, CardDataService]
})
export class CardPage {
    public listCardAll: Array<Card>;
    public listCardTypeAll: Array<Card>;
	public listCard: Array<Card>;
    public listCardData: Array<CardData>;
    public listCardDataId: Array<string>;
    public topicId: string;
    public title: string;
	public type: number;    
    public index: number;
    public size: number;
    public showMore: boolean;

  	constructor(public navCtrl: NavController, public navParams: NavParams, public utilService: UtilService, public cardService: CardService, public cardDataService: CardDataService, public popoverCtrl: PopoverController) {
  		this.listCard = [];  		    
        this.topicId = this.navParams.get("topicId");
        this.title = this.navParams.get("topicName");
        this.listCardDataId = this.navParams.get("childrentIds");
        this.type = 0;
        this.index = 0;
        this.size = 0;
        this.showMore = true;
  	}

  	ngOnInit () {
        this.utilService.showLoading();
        this.cardService.getListByListCardDataId(this.listCardDataId).then((data) => {
            this.listCardAll = data;            
            this.size = data.length;
            this.filterCard(1);
            this.utilService.hiddenLoading();
        })
  	}

    loadMoreTopic (infiniteScroll) {
        setTimeout(() => {
            let moreCard = this.utilService.clone(this.listCardTypeAll).splice(this.index * NUMBER_ITEM, NUMBER_ITEM);
            this.listCard = this.listCard.concat(moreCard);            
            infiniteScroll.complete();
            this.index ++;
            this.checkLoadMore();
        }, 200);
    }

    // Check if length of all topic greater index to must hidden load more effect
    checkLoadMore () {
        this.showMore = this.index * NUMBER_ITEM < this.listCardTypeAll.length
    }

    filterCard (type: number) {
        this.listCardTypeAll = this.listCardAll.filter((card) => {
            return card.status == type;
        });

        this.listCard = [];
        let moreCard = this.utilService.clone(this.listCardTypeAll).splice(this.index * NUMBER_ITEM, NUMBER_ITEM);
        this.listCard = this.listCard.concat(moreCard);        
        this.index = 1;
        this.checkLoadMore();
    }

    changeType (type) {
        this.type = type;
        this.filterCard(type);
    }

    showSetting (myEvent) {
        let popover = this.popoverCtrl.create(Setting);
        popover.present({
            ev: myEvent
        });
    }

    goLearn () {        
        this.cardService.getLimitByListCardDataId(this.listCardDataId).then((data) => {                        
            let listCard = data.filter((item) => {
                return item.hasChild != 1
            })

            if (listCard.length) {
                this.navCtrl.push(FlashCardPage, {
                    listCardDataId: this.listCardDataId,
                    listCard: listCard,
                    listCardAll: data
                });
            } else {                
                this.navCtrl.push(GamePage, {
                    listCardDataId: this.listCardDataId,
                    listCardAll: data
                })
            }
        })        
    }
}
