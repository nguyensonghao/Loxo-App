import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

import { CommentService } from '../../../../shared/services/comment.service';
import { Comment } from '../../../../shared/models/comment';
/*
  Generated class for the Discussion page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  	selector: 'page-discussion',
  	templateUrl: 'discussion.html',
    providers: [CommentService]
})
export class DiscussionPage {	
    public listComment: Array<Comment>;

  	constructor(public navCtrl: NavController, public navParams: NavParams, public commentService: CommentService) {
  		this.listComment = [];
  	}  

  	ngOnInit () {
        this.commentService.getList().subscribe(data => {
            this.listComment = data;
        })  		
  	}

}
