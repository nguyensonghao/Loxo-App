import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import { CardData } from '../models/card-data';
import { UtilService } from './util.service';

@Injectable()
export class CardDataService {
	constructor (private http: Http, public utilService: UtilService) {
		
	}

	getListByTopicId (topicId: string): Promise<CardData[]> {
		if (this.utilService.isMobile()) {

		} else {
			return new Promise((resolve, reject) => {
				this.http.get("assets/data/database/card.json")
					.map(response => response.json())
					.subscribe((data) => {
						let listCard = [];
						for (var i = 0; i < data.length; i++) {
							if (data[i]['parentId'] == topicId)
								listCard.push(data[i]);
						}

						console.log(listCard.length);
						resolve(listCard);
					})
			})
		}
	}
}