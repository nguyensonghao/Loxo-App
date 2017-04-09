import { Face } from './face.model';
import { GameObject } from './game.model';

export class TrueFalseGame extends GameObject {
	public question: Face;
	public answer: Face;		
	public correctAnswer: Face;
	public isCorrect: boolean;	

	constructor (id: number, gameType: string, question: Face, answer: Face, correctAnswer: Face, isCorrect: boolean) {
		super(id, gameType);		
		this.question = question;
		this.answer = answer;		
		this.correctAnswer = correctAnswer;
		this.isCorrect = isCorrect;		
	}
}