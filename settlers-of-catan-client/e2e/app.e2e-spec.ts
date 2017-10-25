import { SettlersOfCatanClientPage } from './app.po';

describe('settlers-of-catan-client App', () => {
  let page: SettlersOfCatanClientPage;

  beforeEach(() => {
    page = new SettlersOfCatanClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
