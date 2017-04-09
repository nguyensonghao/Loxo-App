import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { TopicPage } from '../topic/topic';
import { MydataPage } from '../mydata/mydata';
import { CalendarPage } from '../calendar/calendar';
import { ChallengePage } from '../challenge/challenge';
import { CommunityPage } from '../community/community';
import { StatisticPage } from '../statistic/statistic';
import { SearchPage } from '../search/search';
import { Topic } from '../../shared/models/topic';
import { TopicService } from '../../shared/services/topic.service';
import { TopicPageModel } from './models/topic.page.model';

@Component({
  	selector: 'page-home',
  	templateUrl: 'home.html',
  	providers: [TopicService]
})

export class HomePage {
	public listTopic: Array<Topic>;
	public listTopicPage: Array<TopicPageModel>;
	public loadDone: boolean;

	constructor(public navCtrl: NavController, public navParams: NavParams, public topicService: TopicService) {
		this.loadDone = false;
		this.listTopic = [];
		this.listTopicPage = [];		
	}

	ngOnInit () {
		this.topicService.getListByParentId("5702387421413376").then((data) => {
			this.listTopic = data;
			this.listTopic.map((topic, index) => {
				this.listTopicPage.push(<TopicPageModel>{
					id: index,
					title: topic.name,
					topicId: topic.id,
					page: TopicPage	
				})
			})

			// this.listTopicPage.push(<TopicPageModel>{
			// 	id: 4,
			// 	title: 'My Card',
			// 	topicId: '',
			// 	page: MydataPage
			// })

			this.loadDone = true;
		})
	}

	goPage (page) {
		switch (page) {
			case "calendar":
				this.navCtrl.push(CalendarPage);
				break;
			case "challenge":
				this.navCtrl.push(ChallengePage);
				break;
			case "community":
				this.navCtrl.push(CommunityPage);
				break;
			case "search":
				this.navCtrl.push(SearchPage);
				break;
			default:
				this.navCtrl.push(StatisticPage);
				break;
		}
	}

}
