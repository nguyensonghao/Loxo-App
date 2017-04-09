import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import { Rank } from '../models/rank';

@Injectable()
export class RankService {
	constructor (private http: Http) {
		
	}

	getList (): Observable<Rank[]> {
		return this.http.get("assets/data/rank.json")
			.map(response => response.json() as Rank[]);
	}	
}