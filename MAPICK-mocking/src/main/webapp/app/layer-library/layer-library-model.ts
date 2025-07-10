export class LayerLibraryDTO {

  constructor(data:Partial<LayerLibraryDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  createdAt?: string|null;
  member?: number|null;
  layer?: number|null;

}
