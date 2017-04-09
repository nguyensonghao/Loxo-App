import { Face } from './face.model';
import { MultipleGame } from './multiple.model';
import { GameObject } from './game.model';

export class SentenceGame extends GameObject {    
    public sentence: Face;
    public listQuestion: Array<MultipleGame>;

    constructor (id: number, gameType: string, sentence: Face, listQuestion: Array<MultipleGame>) {
        super(id, gameType);
        this.sentence = sentence;
        this.listQuestion = listQuestion;
    }
}