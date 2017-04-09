import { Face } from './face.model';
import { GameObject } from './game.model';

export class MultipleGame extends GameObject {	
	public question: Face;
	public listAnswer: Array<Face>;
	public correctAnswer: Array<Face>;

	constructor (id: number, gameType: string, question: Face, listAnswer: Array<Face>, correctAnswer: Array<Face>) {
		super(id, gameType);		
		this.question = question;
		this.listAnswer = listAnswer;
		this.correctAnswer = correctAnswer;
	}
}