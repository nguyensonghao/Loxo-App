import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';

import { Comment } from '../models/comment';

@Injectable()
export class CommentService {
	constructor (private http: Http) {
		
	}

	getList (): Observable<Comment[]> {
		return this.http.get("assets/data/comment.json")
			.map(this.mapComment);
	}

	mapComment (response) {
		return response.json().map((comment) => {
			return <Comment>({
				id: comment.id,
				username: comment.username,
				date: comment.date,
				like: comment.like,
				avatar: comment.avatar,
				parentId: comment.parentId
			})
		})		
	}
}