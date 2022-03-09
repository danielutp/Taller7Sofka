<template>
  <q-page class="flex flex-center">
    <q-table
      dense
      :rows="data"
      :filter="filter"
      :columns="columns"
      row-key="id"
      color="primary"
    >
      <template v-slot:top-left>
        <q-btn unelevated rounded icon="add" color="primary" @click="creating" label="Agregar"/>
        <q-space />
      </template>
      <template v-slot:top-right>
        <q-input dense debounce="300" v-model="filter" placeholder="Buscar">
          <template v-slot:append>
            <q-icon name="search" />
          </template>
        </q-input>
      </template>
      <template v-slot:body="props">
        <q-tr :props="props">
          <q-td key="name" :props="props">
            {{ props.row.name }}
          </q-td>
          <q-td key="phone" :props="props">
            {{ props.row.phone }}
          </q-td>
          <q-td key="email" :props="props">
            {{ props.row.email }}
          </q-td>
          <q-td key="date_of_birth" :props="props">
            {{ props.row.date_of_birth }}
          </q-td>
          <q-td key="create_at" :props="props">
            {{ props.row.create_at }}
          </q-td>
          <q-td key="edit" :props="props">
            <q-btn round size="xs" color="primary" icon="border_color" @click="editing(props.row)" />
          </q-td>
          <q-td key="delete" :props="props">
            <q-btn round size="xs" color="negative" icon="delete_forever" @click="onDelete(props.row)" />
          </q-td>
          <q-td key="delete" :props="props">
            <q-btn round size="xs" color="green" icon="delete_forever" @click="onDeletePartial(props.row)" />
          </q-td>
        </q-tr>
      </template>
    </q-table>
    <q-dialog v-model="toolbar" persistent>
      <q-card style="width: 700px; max-width: 80vw;">
        <q-card-section class="row items-center">
          <div class="text-h6">Contacto</div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>
        <q-banner class="bg-grey-3">
          <template v-slot:avatar>
            <q-icon name="warning" color="warning" />
          </template>
          Los campos marcados con (*) son obligatorios
        </q-banner>
        <q-card-section>
          <q-form ref="form" @submit.prevent="">
            <div class="row q-col-gutter-sm">
              <div class="col-md-6">
                <q-input filled v-model="contact.name" label="Nombre *" lazy-rules :rules="[ val => val && val.length > 0 || 'El campo es obligatorio']"/>
              </div>
              <div class="col-md-6">
                <q-input filled v-model="contact.phone" label="Telefono *" lazy-rules :rules="[ val => val && val.length > 0 || 'El campo es obligatorio']"/>
              </div>
              <div class="col-md-6">
                <q-input filled v-model="contact.email" label="Correo *" lazy-rules :rules="[ val => val && val.length > 0 || 'El campo es obligatorio']"/>
              </div>
              <div class="col-md-6">
                <q-input filled v-model="contact.date_of_birth" label="Fecha de nacimiento *" lazy-rules :rules="[ val => val && val.length > 0 || 'El campo es obligatorio']"/>
              </div>
            </div>
          </q-form>
        </q-card-section>
        <q-card-actions align="right" class="bg-white text-teal">
          <q-btn label="Cancelar" v-close-popup color="negative"/>
          <q-btn v-if="!isEditing" label="Guardar" @click.prevent="onSubmit" color="primary"/>
          <q-btn v-else label="Actualizar" @click.prevent="onEdit" color="primary"/>
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
import { defineComponent, onMounted, reactive, ref } from 'vue'
import { api } from 'boot/axios'
import { useQuasar } from 'quasar'

export default defineComponent({
  name: 'PageIndex',
  setup () {
    const data = ref([])
    const filter = ref('')
    const form = ref()
    const isEditing = ref(false)
    const id = ref(null)
    const contact = reactive({
      name: null,
      phone: null,
      email: null,
      date_of_birth: null,
      create_at: null,
      update_at: null,
      delete_at: null
    })
    const toolbar = ref(false)
    const $q = useQuasar()

    const columns = [
      { name: 'name', align: 'center', label: 'Nombre', field: 'name', sortable: true },
      { name: 'phone', align: 'center', label: 'Telefono', field: 'phone', sortable: true },
      { name: 'email', align: 'center', label: 'Correo', field: 'email', sortable: true },
      { name: 'date_of_birth', align: 'center', label: 'Fecha de nacimiento', field: 'date_of_birth', sortable: true },
      { name: 'create_at', align: 'center', label: 'Fecha de creacion', field: 'create_at', sortable: true },
      { name: 'edit', align: 'center', label: 'Editar', field: 'edit', sortable: true },
      { name: 'delete', align: 'center', label: 'Eliminar', field: 'delete', sortable: true },
      { name: 'deletepartial', align: 'center', label: 'Eliminado lógico', field: 'deletepartial', sortable: true }
    ]

    const loadList = () => {
      api.get('contacts').then(response => {
        data.value = response.data.data
      }).catch(e => {
        if (e) {
          console.log(e)
        }
      })
    }

    const onSubmit = () => {
      form.value.validate().then(success => {
        if (success) {
          contact.create_at = new Date()
          api.post('contact/create', contact).then(response => {
            loadList()
            toolbar.value = false
          }).catch(e => {
            if (e) {
              console.log(e)
            }
          })
        }
      })
    }

    const onEdit = () => {
      form.value.validate().then(success => {
        if (success) {
          contact.update_at = new Date()
          api.put(`contact/update/${id.value}`, contact).then(response => {
            loadList()
            toolbar.value = false
          }).catch(e => {
            if (e) {
              console.log(e)
            }
          })
        }
      })
    }

    const onDeletePartial = (row) => {
      $q.dialog({
        title: 'Confirmación',
        message: 'Desea eliminar el registro lógicamente?',
        ok: {
          label: 'Si',
          color: 'positive'
        },
        cancel: {
          label: 'No',
          color: 'negative'
        }
      }).onOk(() => {
        contact.delete_at = new Date()
        api.patch(`contact/updatepartial/${row.id}`, contact).then(response => {
          loadList()
          toolbar.value = false
        }).catch(e => {
          if (e) {
            console.log(e)
          }
        })
      })
    }

    const onDelete = (row) => {
      $q.dialog({
        title: 'Confirmación',
        message: 'Desea eliminar el registro totalmente?',
        ok: {
          label: 'Si',
          color: 'positive'
        },
        cancel: {
          label: 'No',
          color: 'negative'
        }
      }).onOk(() => {
        api.delete(`contact/deletetotal/${row.id}`).then(response => {
          loadList()
          toolbar.value = false
        }).catch(e => {
          if (e) {
            console.log(e)
          }
        })
      })
    }

    const creating = () => {
      contact.name = null
      contact.phone = null
      contact.email = null
      contact.date_of_birth = null
      contact.create_at = null
      toolbar.value = true
      isEditing.value = false
    }

    const editing = (row) => {
      contact.name = row.name
      contact.phone = row.phone
      contact.email = row.email
      contact.date_of_birth = row.date_of_birth
      contact.create_at = row.create_at
      id.value = row.id
      toolbar.value = true
      isEditing.value = true
    }

    onMounted(() => {
      loadList()
    })

    return {
      data,
      contact,
      filter,
      columns,
      toolbar,
      form,
      isEditing,
      onSubmit,
      onDeletePartial,
      onEdit,
      onDelete,
      creating,
      editing
    }
  }
})
</script>
