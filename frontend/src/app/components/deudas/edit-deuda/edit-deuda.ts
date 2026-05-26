import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { DeudaService } from '../../../services/deuda.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-edit-deuda',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './edit-deuda.html'
})
export class EditDeudaComponent implements OnInit {
  form: FormGroup;
  error = '';
  cargando = false;
  id!: number;

  constructor(
    private fb: FormBuilder,
    private deudaService: DeudaService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      persona: ['', Validators.required],
      monto: ['', [Validators.required, Validators.min(1)]],
      descripcion: ['', Validators.required],
      tipo: ['YO_DEBO', Validators.required],
      tipoPago: ['']
    });
  }

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.deudaService.listarPorUsuario(this.authService.getUsuarioId()).subscribe({
      next: deudas => {
        const d = deudas.find(x => x.id === this.id);
        if (d) this.form.patchValue({ persona: d.persona, monto: d.monto, descripcion: d.descripcion, tipo: d.tipo, tipoPago: d.tipoPago || '' });
        else this.error = 'Deuda no encontrada.';
      }
    });
  }

  enviar() {
    if (this.form.invalid) return;
    this.cargando = true;
    const val = this.form.value;
    this.deudaService.editar(this.id, {
      persona: val.persona,
      monto: Number(val.monto),
      descripcion: val.descripcion,
      tipo: val.tipo,
      tipoPago: val.tipoPago || undefined,
      usuarioId: this.authService.getUsuarioId()
    }).subscribe({
      next: () => this.router.navigate(['/deudas']),
      error: err => { this.error = err.error || 'Error al guardar'; this.cargando = false; }
    });
  }
}
