$(function () {
  console.log("counters list page");

  $('#submit-filter-btn').on('click', function(e) {
    var checkedValues = [];
    $(".type-filter:checked").each(function(){
      checkedValues.push($(this).val());
    });
    //this.href = this.href + '?type=' + checkedValues.join();

    $.ajax({
      url: contextUrl + 'list/fragment/?type=' + checkedValues.join(),
      method: 'GET'
    }).success(function (data) {
      $('#counters_table_full').html(data);
    });

    e.preventDefault();
    
  });

  $('.delete_counter').on('click', function (e) {
    var counter_id = $(this).data('id');
    console.log('delete counter ' + counter_id);
    var $counterRow = $(this).closest('tr');

    $.ajax({
      url: contextUrl + 'counters/' + counter_id,
      method: 'DELETE'
    }).success(function () {
      console.log('counter deleted: ' + counter_id);
      showNotification(true, '');
      
      console.log($counterRow);
      $counterRow.remove();

    }).fail(function (xhr, statusText) {
      console.log('failed to delete counter: ' + counter_id);
      showNotification(false, statusText);
    });

  });

  function showNotification(isSuccess, msg) {

    var alertClass = 'alert-success';

    if (isSuccess) {
      msg = 'Counter successfully deleted. ' + msg;
    } else {
      msg = 'Error deleting counter. ' + msg;
      alertClass = 'alert-danger';
    }

    var notification = '<div class="alert alert-dismissible fade in ' + alertClass + '"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' + msg + '</div>';
    
    $notifications = $('#notifications');

    $notifications.html(notification);
    $notifications.alert();

  };


});
