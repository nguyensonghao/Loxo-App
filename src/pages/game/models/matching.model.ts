import { Box } from './box.model';
import { GameObject } from './game.model';

export class MatchingGame extends GameObject {	
	public listBox: Array<Box>;	
	public numberChoice: number;

	constructor (id: number, gameType: string, listBox: Array<Box>) {
		super(id, gameType);		
		this.listBox = listBox;
		this.numberChoice = 6;
	}
}