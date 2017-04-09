import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Platform } from 'ionic-angular';
import { SQLite, SQLiteObject } from '@ionic-native/sqlite';

@Injectable()
export class DatabaseService {
	public db: SQLiteObject;

	constructor (public sqlite: SQLite, public platform: Platform) {
		this.platform.ready().then(() => {                      
            this.sqlite.create({
                name: 'toeic.sqlite',
                location: 'default',
                createFromLocation: 1
            }).then((db: SQLiteObject) => {
                this.db = db;
            }).catch(e => console.log(e));       
        });     
	}
}