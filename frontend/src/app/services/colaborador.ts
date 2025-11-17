export interface ItemCafe {
  id?: number;
  descricao: string;
  trouxe: boolean;
  dataCafe: string;
  editando?: boolean;
}


export interface Colaborador {
  id?: number;
  nome: string;
  cpf: string;
  dataCafe: string;
  itensCafe?: ItemCafe[];
  editando?: boolean;
}
