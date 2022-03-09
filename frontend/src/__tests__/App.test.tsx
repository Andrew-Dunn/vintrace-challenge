import React from 'react';
import { render, screen } from '@testing-library/react';
import App from '../App';
import {RestCellarDoor} from "../clients/rest/RestCellarDoor";
import {CellarDoorProvider} from "../components/CellarDoorContext";
import {setupServer} from "msw/node";
import {rest} from "msw";

const server = setupServer(
    rest.get('/api/wine/FOOBAR-1234', (req, res, ctx) => {
        return res(ctx.json({
            "lotCode":"FOOBAR-1234",
            "description":"2011 Yarra Valley Chardonnay",
            "volume":"1,000 L",
            "tankCode":"T25-01",
            "productState":"Ready for bottling",
            "ownerName":"YV Wines Pty Ltd"}));
    }),
    rest.get('/api/wine/FOOBAR-1235', (req, res, ctx) => {
        return res(ctx.status(404));
    }),
)

beforeAll(() => server.listen())
afterEach(() => server.resetHandlers())
afterAll(() => server.close())

test('renders wine lot id', () => {
  window.history.pushState({}, 'WinePage', '/wine/FOOBAR-1234');
  renderWithRestCellarDoor();
  const heading = screen.getByRole("heading", {name: "FOOBAR-1234"});
  expect(heading).toBeInTheDocument();
});

test('renders wine lot description', async () => {
  window.history.pushState({}, 'WinePage', '/wine/FOOBAR-1234');
  renderWithRestCellarDoor();
  const description = await screen.findByText(/2011 Yarra Valley Chardonnay/i);
  expect(description).toBeInTheDocument();
});

test('displays error if wine not found', async () => {
    window.history.pushState({}, 'WinePage', '/wine/FOOBAR-1235');
    renderWithRestCellarDoor();
    const description = await screen.findByText(/Wine not found/i);
    expect(description).toBeInTheDocument();
});

const renderWithRestCellarDoor = () => {
    // We could theoretically have created a mock/fake cellar door for testing, but using MSW
    // is a bit easier and tests the REST implementation.
    render(
        <CellarDoorProvider cellarDoor={new RestCellarDoor("")}>
            <App />
        </CellarDoorProvider>);
};