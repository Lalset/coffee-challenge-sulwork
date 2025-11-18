import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

import { Colaborador, ItemCafe } from './colaborador';


@Injectable({
  providedIn: 'root'
})
export class ColaboradorService {

  private api = environment.apiUrl;
  private apiColaboradores = `${this.api}/colaboradores`;
  private apiItensCafe = `${this.api}/itens-cafe`;

  constructor(private http: HttpClient) {}

  // COLABORADORES
  listar(): Observable<Colaborador[]> {
    return this.http.get<Colaborador[]>(this.apiColaboradores);
  }

  cadastrar(colaborador: Colaborador): Observable<string> {
    return this.http.post(this.apiColaboradores, colaborador, {
      responseType: 'text'
    });
  }

  atualizar(id: number, colaborador: Colaborador): Observable<string> {
    return this.http.put(`${this.apiColaboradores}/${id}`, colaborador, {
      responseType: 'text'
    });
  }

  deletar(id: number): Observable<string> {
    return this.http.delete(`${this.apiColaboradores}/${id}`, {
      responseType: 'text'
    });
  }

  // ITENS DE CAFÉ

  listarItensPorData(data: string): Observable<ItemCafe[]> {
    return this.http.get<ItemCafe[]>(`${this.apiItensCafe}/data/${data}`);
  }

  atualizarTrouxe(id: number, trouxe: boolean): Observable<string> {
    return this.http.put(`${this.apiItensCafe}/${id}/trouxe/${trouxe}`, {}, {
      responseType: 'text'
    });
  }

  // Atualizar Item
  atualizarItem(item: ItemCafe): Observable<string> {
  return this.http.put(
    `${this.apiItensCafe}/${item.id}`,
    item,
    { responseType: 'text' }
  );
}

  // DELETE item
  deletarItem(id: number): Observable<string> {
    return this.http.delete(`${this.apiItensCafe}/${id}`, {
      responseType: 'text'
    });
  }
}
