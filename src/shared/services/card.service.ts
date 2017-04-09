import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import { Card } from '../models/card';
import { UtilService } from './util.service';
import { LocalStorageService } from './localStorage.service';

@Injectable()
export class CardService {
	constructor (private http: Http, public utilService: UtilService, public localStorageService: LocalStorageService) {
		
	}	

	getListByListCardDataId (listCardDataId: Array<string>): Promise<Card[]> {
		if (this.utilService.isMobile()) {

		} else {
			return new Promise((resolve, reject) => {
				this.http.get("assets/data/database/card.json")
					.map(response => response.json())
					.subscribe((data) => {
						let listCard = [];												
						data.map((card) => {
							if (listCardDataId.indexOf(card['parentId']) != -1)
								listCard.push(this.mapCard(card));
						})
						resolve(listCard);
					})
			})
		}
	}

	getLimitByListCardDataId (listCardDataId: Array<string>): Promise<Card[]> {
		if (this.utilService.isMobile()) {

		} else {
			return new Promise((resolve, reject) => {
				this.http.get("assets/data/database/card.json")
					.map(response => response.json())
					.subscribe((data) => {
						let listCard = [];						
						let numberQuestion = this.localStorageService.getItem('numberQuestion');
						numberQuestion = numberQuestion ? numberQuestion : 15;
						let index = 0;
						
						while (listCard.length < numberQuestion) {
							if (listCardDataId.indexOf(data[index]['parentId']) != -1)								
								listCard.push(this.mapCard(data[index]));
							
							index ++;
						}
												
						resolve(listCard);
					})
			})
		}
	}

	getListByCardDataId (cardDataId: string, limit: number = 15): Promise<Card[]> {
		if (this.utilService.isMobile()) {

		} else {
			return new Promise((resolve, reject) => {
				this.http.get("assets/data/database/card.json")
					.map(response => response.json())
					.subscribe((data) => {
						let listCard = [];
						if (data.length < limit)
							limit = data.length;

						for (var i = 0; i < limit; i++) {
							if (cardDataId == data[i]['parentId'])								 
								listCard.push(this.mapCard(data[i]));							
						}
						
						resolve(listCard);
					})
			})
		}
	}

	getListCardChild (cardId: string): Promise<Card[]> {
		if (this.utilService.isMobile()) {

		} else {
			return new Promise((resolve, reject) => {
				this.http.get("assets/data/database/card.json")
					.map(response => response.json())
					.subscribe((data) => {
						let listCard = [];
						for (var i = 0; i < data.length; i++) {
							if (cardId == data[i]['parentId'])								 
								listCard.push(this.mapCard(data[i]));							
						}
						
						resolve(listCard);
					})
			})
		}
	}

	getListCardChildById (listCardId: Array<string>): Promise<Card[]> {
		if (this.utilService.isMobile()) {

		} else {
			return new Promise((resolve, reject) => {
				this.http.get("assets/data/database/card.json")
					.map(response => response.json())
					.subscribe((data) => {
						let listCard = [];
						for (var i = 0; i < data.length; i++) {
							if (listCardId.indexOf(data[i]['parentId']) != -1)								 
								listCard.push(this.mapCard(data[i]));							
						}
						
						resolve(listCard);
					})
			})
		}
	}

	private mapCard (item: any): Card {
		return <Card> {
			"id": item.id,
			"orderIndex": item.orderIndex,
			"parentId": item.parentId,
			"userId": item.userId,
			"difficultyLevel": item.difficultyLevel,
			"status": item.status,
			"type": item.type,
			"hasChild": item.hasChild,
			"frontText": item.frontText,
			"frontImage": item.frontImage,
			"frontSound": item.frontSound,
			"frontVideo": item.frontVideo,
			"frontHint": item.frontHint,
			"frontLanguage": item.frontLanguage,
			"backText": item.backText,
			"backImage": item.backImage,
			"backSound": item.backSound,
			"backVideo": item.backVideo,
			"backHint": item.backHint,
			"backLanguage": item.backLanguage,
			"lastUpdate": item.lastUpdate,
			"backTexts": JSON.parse(item.backTexts),			
			"multiChoices": JSON.parse(item.multiChoices),
			"typeOfWord": ""
		}
	}
}