import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ColaboradorService } from '../../services/colaborador.service';
import { Colaborador, ItemCafe } from '../../services/colaborador';
import { Router } from '@angular/router';
import { forkJoin, Observable } from 'rxjs';

@Component({
  selector: 'app-lista',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lista.html',
  styleUrls: ['./lista.css']
})
export class Lista implements OnInit {

  colaboradores: Colaborador[] = [];
  colaboradoresFiltrados: Colaborador[] = [];
  carregando = false;
  dataFiltro = '';

  private backup: Map<number, Colaborador> = new Map();

  constructor(
    private colaboradorService: ColaboradorService,
    private router: Router
  ) {}

  ngOnInit() {
    this.carregarLista();
  }

  private ajustarData(dataStr: string): string {
    const data = new Date(dataStr);
    const corrigida = new Date(data.getTime() + data.getTimezoneOffset() * 60000);
    return corrigida.toISOString().split('T')[0];
  }

  private getMensagemErro(err: any): string {
    if (!err) return "Erro desconhecido.";
    if (typeof err === "string") return err;
    if (err.error) {
      if (typeof err.error === "string") return err.error;
      if (err.error.message) return err.error.message;
    }
    if (err.message) return err.message;
    try { return JSON.stringify(err); }
    catch { return "Erro desconhecido."; }
  }

  carregarLista() {
    this.carregando = true;

    this.colaboradorService.listar().subscribe({
      next: lista => {
        this.colaboradores = lista.map(c => ({
          ...c,
          itensCafe: c.itensCafe ?? [],
          editando: false
        }));
        this.colaboradoresFiltrados = [...this.colaboradores];
        this.carregando = false;
      },
      error: err => {
        alert("Erro ao carregar: " + this.getMensagemErro(err));
        this.carregando = false;
      }
    });
  }

  filtrar() {
    if (!this.dataFiltro) {
      this.colaboradoresFiltrados = [...this.colaboradores];
      return;
    }

    this.colaboradoresFiltrados = this.colaboradores.filter(
      c => c.dataCafe === this.dataFiltro
    );
  }

  marcarTrouxe(item: ItemCafe) {
    item.trouxe = !item.trouxe;
    this.colaboradorService.atualizarTrouxe(item.id!, item.trouxe!).subscribe({
      next: () => {},
      error: err => alert('Erro ao atualizar: ' + (err?.error || err.message))
    });
  }

  removerColaborador(colaborador: Colaborador) {
    if (!confirm(`Deseja realmente remover ${colaborador.nome}?`)) return;

    this.colaboradorService.deletar(colaborador.id!).subscribe({
      next: () => this.carregarLista(),
      error: () => this.carregarLista()
    });
  }

  removerItem(item: ItemCafe, colab: Colaborador) {
    if (!confirm(`Remover item "${item.descricao}"?`)) return;

    this.colaboradorService.deletarItem(item.id!).subscribe({
      next: () => {
        colab.itensCafe = colab.itensCafe!.filter(i => i.id !== item.id);
      },
      error: err => alert("Erro ao remover item: " + this.getMensagemErro(err))
    });
  }

  editarColaborador(c: Colaborador) {
    c.editando = true;
    this.backup.set(c.id!, structuredClone(c)); // clone seguro
  }

  cancelarEdicao(c: Colaborador) {
    const original = this.backup.get(c.id!);
    if (original) Object.assign(c, original);
    c.editando = false;
  }

  salvarEdicao(c: Colaborador) {
    const original = this.backup.get(c.id!);
    const dataMudou = !!original && original.dataCafe !== c.dataCafe;

    c.dataCafe = this.ajustarData(c.dataCafe);

    this.colaboradorService.atualizar(c.id!, c).subscribe({
      next: () => {
        const tarefas: Observable<any>[] = [];

        c.itensCafe?.forEach(item => {
          if (dataMudou) {
            item.dataCafe = this.ajustarData(c.dataCafe);
          }

          if (!item.descricao || item.descricao.trim() === '') {
            console.warn(`Ignorando item sem descrição (id: ${item.id})`);
            return;
          }

          tarefas.push(this.colaboradorService.atualizarItem(item));
        });

        if (tarefas.length > 0) {
          forkJoin(tarefas).subscribe({
            next: () => {
              alert("Alterações salvas!");
              c.editando = false;
              this.carregarLista();
            },
            error: err => {
              alert("Erro ao atualizar itens: " + this.getMensagemErro(err));
            }
          });
        } else {
          alert("Alterações salvas!");
          c.editando = false;
          this.carregarLista();
        }
      },
      error: err => {
        alert("Erro ao salvar colaborador: " + this.getMensagemErro(err));
      }
    });
  }

  irParaCadastro() {
    this.router.navigate(['/cadastro']);
  }
}
