import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ColaboradorService } from '../../services/colaborador.service';
import { Colaborador, ItemCafe } from '../../services/colaborador';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cadastro.html',
  styleUrls: ['./cadastro.css']
})
export class Cadastro {

  nome = '';
  cpf = '';
  dataCafe = '';
  descricaoItem = '';

  itensCafe: ItemCafe[] = [];

  constructor(
    private colaboradorService: ColaboradorService,
    private router: Router
  ) {}

  irParaLista() {
    this.router.navigate(['/lista']);
  }

  private alerta(msg: string) {
    alert(msg);
  }

  // === formata o CPF ===
  formatarCPF() {
    let v = this.cpf.replace(/\D/g, '');
    if (v.length > 11) v = v.substring(0, 11);

    if (v.length > 9)
      this.cpf = v.replace(/(\d{3})(\d{3})(\d{3})(\d{0,2})/, '$1.$2.$3-$4');
    else if (v.length > 6)
      this.cpf = v.replace(/(\d{3})(\d{3})(\d{0,3})/, '$1.$2.$3');
    else if (v.length > 3)
      this.cpf = v.replace(/(\d{3})(\d{0,3})/, '$1.$2');
    else
      this.cpf = v;
  }

  private ajustarData(dataStr: string): string {
    const data = new Date(dataStr);
    const corrigida = new Date(data.getTime() + data.getTimezoneOffset() * 60000);
    return corrigida.toISOString().split('T')[0];
  }

  adicionarItem() {
    const descricao = this.descricaoItem.trim();
    const data = this.dataCafe;

    if (!descricao) return this.alerta('A descrição do item não pode ser vazia.');
    if (!data) return this.alerta('Escolha a data antes de adicionar itens.');

    const repetido = this.itensCafe.some(
      item =>
        item.descricao.toLowerCase() === descricao.toLowerCase() &&
        item.dataCafe === data
    );

    if (repetido) return this.alerta('Este item já foi adicionado para esta data.');

    this.itensCafe.push({
      descricao,
      dataCafe: data,
      trouxe: false
    });

    this.descricaoItem = '';
  }

  removerItem(i: number) {
    this.itensCafe.splice(i, 1);
  }

  cadastrar() {
    if (!this.nome.trim()) return this.alerta('O nome é obrigatório.');

    const cpfLimpo = this.cpf.replace(/\D/g, '');
    if (!/^\d{11}$/.test(cpfLimpo)) {
      return this.alerta('CPF inválido. Deve conter 11 dígitos.');
    }

    if (!this.dataCafe) return this.alerta('Selecione a data do café.');

    const hoje = new Date(); hoje.setHours(0, 0, 0, 0);
    const data = new Date(this.dataCafe); data.setHours(0, 0, 0, 0);

    if (data <= hoje) return this.alerta('A data do café deve ser maior que hoje.');

    if (this.itensCafe.length === 0) {
      return this.alerta('Adicione pelo menos um item do café.');
    }

    const body: Colaborador = {
      nome: this.nome.trim(),
      cpf: cpfLimpo,
      dataCafe: this.ajustarData(this.dataCafe),
      itensCafe: this.itensCafe.map(i => ({
        descricao: i.descricao,
        dataCafe: this.ajustarData(i.dataCafe),
        trouxe: i.trouxe,
        colaboradorId: null
      }))
    };

    this.colaboradorService.cadastrar(body).subscribe({
      next: () => {
        this.alerta('Colaborador cadastrado com sucesso!');
        this.resetarFormulario();
      },
      error: err =>
        this.alerta('Erro: ' + (err?.error?.message || err?.message || 'Desconhecido'))
    });
  }

  resetarFormulario() {
    this.nome = '';
    this.cpf = '';
    this.dataCafe = '';
    this.descricaoItem = '';
    this.itensCafe = [];
  }

}
