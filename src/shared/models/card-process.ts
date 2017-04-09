export class CardProcess {
    public id: string;
	public userId: string;
	public parentId: string;
	public cardId: string;
	public lastUpdate: number;
	public status: number;						
	public difficultyLevel: number;
	public languageId: number;
	public history: Array<number>;
}