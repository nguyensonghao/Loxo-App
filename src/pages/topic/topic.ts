import { Component } from '@angular/core';
import { NavController, NavParams, App } from 'ionic-angular';

import { FlashCardPage } from '../flashcard/flashcard';
import { CardPage } from '../card/card';
import { Topic } from '../../shared/models/topic';
import { TopicService } from '../../shared/services/topic.service';
import { UtilService } from '../../shared/services/util.service';
import { LoginPage } from '../login/login';


@Component({
	selector: 'page-topic',
	templateUrl: 'topic.html',
    providers: [TopicService, UtilService]
})

export class TopicPage {
    public listTopic: Array<Topic>;
    public listTopicAll: Array<Topic>;
    public topicId: string;
    public topic: Topic;
    public index: number;
    public showMore: boolean;

    constructor(public navCtrl: NavController, public navParams: NavParams, public topicService: TopicService, public utilService: UtilService, public appCtrl: App) {
        this.navCtrl = navCtrl;
        this.listTopic = [];
        this.listTopicAll = [];
        this.topic = new Topic();
        this.topicId = this.navParams.data;
        this.index = 0;
        this.showMore = true;
    }

    ngOnInit () {
        this.utilService.showLoading();
    	this.topicService.getListByParentId(this.topicId).then((data) => {
            this.listTopicAll = data;
            this.listTopic = this.utilService.clone(this.listTopicAll).splice(this.index, 10);
            this.index = 1;
            this.checkLoadMore();
            this.utilService.hiddenLoading();
        })
    }

    loadMoreTopic (infiniteScroll) {
        setTimeout(() => {
            let moreTopic = this.utilService.clone(this.listTopicAll).splice(this.index * 10, 10);
            this.listTopic = this.listTopic.concat(moreTopic);
            infiniteScroll.complete();
            this.index ++;
            this.checkLoadMore();
        }, 200);
    }

    // Check if length of all topic greater index to must hidden load more effect
    checkLoadMore () {
        this.showMore = this.index * 10 < this.listTopicAll.length
    }

    goCard (topic) {
        this.appCtrl.getRootNav().push(CardPage, {
            topicId: topic['id'],
            topicName: topic['name'],
            childrentIds: topic['childrentIds']
        })
    }    
}
