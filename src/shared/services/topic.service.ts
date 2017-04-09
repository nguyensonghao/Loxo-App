import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/filter';
import { SQLite, SQLiteObject } from '@ionic-native/sqlite';

import { Topic } from '../models/topic';
import { UtilService } from './util.service';

@Injectable()
export class TopicService {
	constructor (private http: Http, private utilService: UtilService) {
		
	}

	getList(): Promise<Topic[]> {
		if (this.utilService.isMobile()) {

		} else {
			return new Promise((resolve, reject) => {
				this.http.get("assets/data/database/topic.json")
					.map(response => response.json())
					.subscribe((data) => {
						resolve(data);
					})				
			})		
		}		
	}

	getById (id: string): Promise<Topic> {
		if (this.utilService.isMobile()) {

		} else {
			return new Promise((resolve, reject) => {
				this.http.get("assets/data/database/topic.json")
					.map(response => response.json())
					.subscribe((data) => {
						for (var i = 0; i < data.length; i++) {
							if (data[i]['id'] == id)
								resolve(data[i]);
						}
												
					})	
			})
		}
	}

	getListByParentId (parentId: string): Promise<Topic[]> {
		if (this.utilService.isMobile()) {

		} else {
			return new Promise((resolve, reject) => {
				this.http.get("assets/data/database/topic.json")
					.map(response => response.json())
					.subscribe((data) => {
						let result = [];
						for (var i = 0; i < data.length; i++) {
							if (data[i]['parentId'] == parentId)
								result.push(data[i]);
						}
						
						resolve(result);
					})	
			})
		}
	}

	private convertTopic(data: Array<any>): Topic[] {
		let listTopic: Array<Topic> = [];
		data.map((item) => {
			listTopic.push({
				"id": item.id,
				"userId": item.userId,
				"userName": item.userName,
				"parentId": item.parentId,
				"lastUpdate": item.lastUpdate,
				"level": item.level,
				"status": item.status,
				"appId": item.appId,
				"price": item.price,
				"priority": item.priority,
				"name": item.name,
				"description": item.description,
				"avatar": item.avatar,
				"childrentIds": item.childrentIds,
				"totalCardNum": item.totalCardNum,
				"languageId": item.languageId
			})
		})

		return listTopic;
	}
}