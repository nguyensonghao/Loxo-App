import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { GameObject } from '../../models/game.model';

// import { Card } from '../../../../shared/models/card';
declare var $: any;

@Component({
    selector: 'side-header',
    templateUrl: 'side-header.html'
})

export class SideHeader {
    @Input() numberCard: number;    
    @Input() index: number;    
    @Input() history: Array<any>;
    @Input() currentGame: GameObject;    
    @Output() changePage = new EventEmitter<number>();
    public listIndex: Array<Object>;

    constructor(public navCtrl: NavController, public navParams: NavParams) {                
        this.listIndex = [];
    }

    ngOnInit () {
        for (var i = 0; i < this.numberCard; i++) {
            this.listIndex.push({
                index: i,
                status: 0
            });
        }                      
    }
    

    ngOnChanges () {        
        $('.list-page').scrollLeft(this.index * 30 - $('.list-page').width() / 2);
        this.history.map((history) => {
            for (var i = 0; i < this.listIndex.length; i++) {
                if (this.listIndex[i]['index'] == history['index']) {
                    if (history['result'] == null) {
                        this.listIndex[i]['status'] = 0;
                    } else {                        
                        this.listIndex[i]['status'] = history['result'] ? 1 : -1; 
                    }
                }
            }            
        })          
    }

    changePageItem (id: number) {                 
        this.changePage.emit(id);
    }

    getClass (status: number): string {
        switch (status) {
            case 0: 
                return ''
            case -1:
                return 'answer-wrong';                
            default:
                return 'answer-correct';
        }
    }
}
